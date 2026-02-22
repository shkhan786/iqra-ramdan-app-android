package com.shkhan.iqra.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shkhan.iqra.engine.HeroPhaseEngine
import com.shkhan.iqra.ui.alarm.AlarmPlayer
import com.shkhan.iqra.ui.model.*
import com.shkhan.iqra.ui.repository.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PrayerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PrayerRepository()
    private val duaRepository = DuaRepository(application.applicationContext)
    private val quoteRepository = QuoteRepository(application.applicationContext)
    private val phaseEngine = HeroPhaseEngine()
    private val userFlags = UserPhaseFlags()
    // ===============================
    // Alarm + Confirmation Flags
    // ===============================
    private var sehriAlarmHandled = false
    private var iftarAlarmHandled = false

    private var sehriConfirmed = false
    private var iftarConfirmed = false




    // ===============================
    // Prayer Timings
    // ===============================
    private val _timings = MutableStateFlow<TimingsData?>(null)
    val timings: StateFlow<TimingsData?> = _timings

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    // ===============================
    // Live Clock
    // ===============================
    private val _currentTime = MutableStateFlow(System.currentTimeMillis())

    // ===============================
    // Alarm State
    // ===============================
    private val _alarmActive = MutableStateFlow(false)
    val alarmActive: StateFlow<Boolean> = _alarmActive

    private var lastAlarmPhase: HeroPhase? = null

    // ===============================
    // Fasting Status
    // ===============================
    private val _fastingStatus = MutableStateFlow(FastingStatus.FASTING)
    val fastingStatus: StateFlow<FastingStatus> = _fastingStatus

    private val _completedDays = MutableStateFlow(4)
    val completedDays: StateFlow<Int> = _completedDays

    val totalDays = 30

    val streak: StateFlow<Int> = combine(
        _completedDays,
        _fastingStatus
    ) { completed, status ->
        when (status) {
            FastingStatus.FASTING -> completed
            FastingStatus.BROKE_FAST -> 0
            FastingStatus.NOT_FASTING -> completed
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        _completedDays.value
    )

    fun updateFastingStatus(status: FastingStatus) {
        _fastingStatus.value = status
    }

    // ===============================
    // Message Engine
    // ===============================
    private val _displayMessage = MutableStateFlow("")
    private val _messageType = MutableStateFlow(MessageType.CONTEXT)

    private var lastMessageMinute: Long = -1
    private var lastMessageType: MessageType? = null

    // ===============================
    // HERO STATE
    // ===============================
    val heroState: StateFlow<HeroState?> =
        combine(_timings, _currentTime) { timings, now ->

            if (timings == null) return@combine null

            val sehriMillis = convertToMillis(timings.fajr)
            val maghribMillis = convertToMillis(timings.maghrib)
            val dhuhrMillis = convertToMillis(timings.dhuhr)
            val asrMillis = convertToMillis(timings.asr)

            val phase = phaseEngine.calculatePhase(
                currentTime = now,
                sehriTime = sehriMillis,
                fajrTime = sehriMillis,
                dhuhrTime = dhuhrMillis,
                asrTime = asrMillis,
                maghribTime = maghribMillis
            )

//            val phase = HeroPhase.IFTAR_ALARM

            handleAlarmState(phase)

            val (contextText, type) = resolveMessage(phase, now)
            updateMessageIfNeeded(type, contextText, now)

            buildHeroState(
                phase,
                timings,
                sehriMillis,
                maghribMillis
            )

        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    // ===============================
    // INIT
    // ===============================
    init {
        loadPrayerTimes()
        startClock()
    }

    private fun loadPrayerTimes() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _timings.value = repository.getTodayPrayerTimes()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun startClock() {
        viewModelScope.launch {
            while (true) {
                _currentTime.value = System.currentTimeMillis()
                delay(1000)
            }
        }
    }

    // ===============================
    // Alarm Logic
    // ===============================
    private fun handleAlarmState(phase: HeroPhase) {

        val isSehriAlarm = phase == HeroPhase.SEHRI_ALARM
        val isIftarAlarm = phase == HeroPhase.IFTAR_ALARM

        // 🔔 START ALARM ONLY IF NOT HANDLED
        if (isSehriAlarm && !sehriAlarmHandled && !_alarmActive.value) {
            _alarmActive.value = true
            AlarmPlayer.play(getApplication())
        }

        if (isIftarAlarm && !iftarAlarmHandled && !_alarmActive.value) {
            _alarmActive.value = true
            AlarmPlayer.play(getApplication())
        }

        // 🧹 Reset flags when leaving phase
        if (!isSehriAlarm) {
            sehriAlarmHandled = false
        }

        if (!isIftarAlarm) {
            iftarAlarmHandled = false
        }
    }

    fun stopAlarm(currentPhase: HeroPhase) {

        AlarmPlayer.stop()
        _alarmActive.value = false

        when (currentPhase) {
            HeroPhase.SEHRI_ALARM -> sehriAlarmHandled = true
            HeroPhase.IFTAR_ALARM -> iftarAlarmHandled = true
            else -> {}
        }
    }

    // ===============================
    // Message Resolver
    // ===============================
    private fun resolveMessage(
        phase: HeroPhase,
        now: Long
    ): Pair<String, MessageType> {

        return when (phase) {

            HeroPhase.SEHRI_WINDOW,
            HeroPhase.SEHRI_LAST_5_MIN ->
                Pair("Make Niyyah and recite Sehri dua.", MessageType.SEHRI_DUA)

            HeroPhase.IFTAR_PRE_WINDOW ->
                Pair("Prepare for dua before breaking your fast.", MessageType.IFTAR_DUA)

            HeroPhase.FASTING_DAY -> {
                val minute = now / (1000 * 60)
                if (minute % 2 == 0L)
                    Pair("Stay patient and focused during your fast.", MessageType.CONTEXT)
                else
                    Pair("", MessageType.QUOTE)
            }

            else ->
                Pair("Stay consistent in worship.", MessageType.CONTEXT)
        }
    }

    private fun updateMessageIfNeeded(
        type: MessageType,
        contextText: String,
        now: Long
    ) {

        val minute = now / (1000 * 60)

        if (minute == lastMessageMinute && type == lastMessageType) return

        lastMessageMinute = minute
        lastMessageType = type

        when (type) {
            MessageType.QUOTE -> {
                val quote = quoteRepository.getRandomQuote()
                _displayMessage.value =
                    quote?.text ?: "Stay consistent in worship."
            }
            else -> {
                _displayMessage.value = contextText
            }
        }

        _messageType.value = type
    }

    // ===============================
    // Build Hero State
    // ===============================
    private fun buildHeroState(
        phase: HeroPhase,
        timings: TimingsData,
        sehriMillis: Long,
        maghribMillis: Long
    ): HeroState {

        val currentMessageType = _messageType.value

        val dua = when (currentMessageType) {
            MessageType.SEHRI_DUA -> duaRepository.getSehriDua()
            MessageType.IFTAR_DUA -> duaRepository.getIftarDua()
            else -> null
        }

        return HeroState(
            phase = phase,
            title = when (phase) {
                HeroPhase.SEHRI_WINDOW -> "Sehri Time"
                HeroPhase.IFTAR_PRE_WINDOW -> "Iftar Soon"
                HeroPhase.IFTAR_ALARM -> "Iftar Time"
                else -> "Iftar in"
            },
            countdownTarget = when (phase) {
                HeroPhase.SEHRI_WINDOW -> sehriMillis
                else -> maghribMillis
            },
            targetLabel = "Iftar Starts",
            targetTime = timings.maghrib,
            secondaryLabel = "Sehri Ends",
            secondaryTime = timings.fajr,
            nextPrayerLabel = "Dhuhr",
            nextPrayerTime = timings.dhuhr,
            displayMessage = if (dua == null) _displayMessage.value else "",
            messageType = currentMessageType,
            buttonType = when {
                _alarmActive.value -> HeroButtonType.STOP_ALARM
                phase == HeroPhase.SEHRI_WINDOW -> HeroButtonType.DONE_SEHRI
                else -> HeroButtonType.NONE
            },
            alarmActive = _alarmActive.value,
            duaTitle = dua?.title,
            duaText = dua?.transliteration,
            duaTranslation = dua?.translation
        )
    }

    // ===============================
    // Time Conversion
    // ===============================
    private fun convertToMillis(time: String): Long {
        return try {
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = formatter.parse(time)
            val calendar = Calendar.getInstance()
            calendar.time = date!!
            calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
            calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH))
            calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            calendar.timeInMillis
        } catch (e: Exception) {
            0L
        }
    }

    fun confirmDone(currentPhase: HeroPhase) {

        when (currentPhase) {

            HeroPhase.SEHRI_ALARM,
            HeroPhase.SEHRI_WINDOW -> {
                sehriConfirmed = true
                sehriAlarmHandled = true
            }

            HeroPhase.IFTAR_ALARM -> {
                iftarConfirmed = true
                iftarAlarmHandled = true
                _completedDays.value += 1
            }

            else -> {}
        }
    }
}