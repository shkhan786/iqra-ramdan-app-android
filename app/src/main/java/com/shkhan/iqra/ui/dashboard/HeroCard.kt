package com.shkhan.iqra.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shkhan.iqra.ui.model.HeroButtonType
import com.shkhan.iqra.ui.model.HeroPhase
import com.shkhan.iqra.ui.model.HeroState
import com.shkhan.iqra.ui.model.MessageType
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HeroCard(
    state: HeroState,
    onStopClick: (HeroPhase) -> Unit,
    onDoneClick: (HeroPhase) -> Unit
) {

    // =========================
    // Live Clock (Countdown)
    // =========================
    val currentTime by produceState(initialValue = System.currentTimeMillis()) {
        while (true) {
            value = System.currentTimeMillis()
            delay(1000)
        }
    }

    val countdown = state.countdownTarget?.let {
        val diff = it - currentTime
        if (diff <= 0) "00:00:00"
        else {
            val hours = diff / (1000 * 60 * 60)
            val minutes = (diff / (1000 * 60)) % 60
            val seconds = (diff / 1000) % 60
            String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        }
    } ?: "--:--:--"

    fun formatTo12Hour(time: String): String {
        return try {
            val input = SimpleDateFormat("HH:mm", Locale.getDefault())
            val output = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val date = input.parse(time)
            output.format(date!!)
        } catch (e: Exception) {
            time
        }
    }

    fun phaseIcon(phase: HeroPhase) = when (phase) {
        HeroPhase.NIGHT_PRE_SEHRI,
        HeroPhase.NIGHT_AFTER_IFTAR -> Icons.Filled.DarkMode
        HeroPhase.SEHRI_WINDOW -> Icons.Filled.WbTwilight
        HeroPhase.SEHRI_LAST_5_MIN -> Icons.Filled.Schedule
        HeroPhase.SEHRI_DONE_EARLY -> Icons.Filled.CheckCircle
        HeroPhase.SEHRI_ALARM -> Icons.Filled.Alarm
        HeroPhase.SEHRI_STOPPED -> Icons.Filled.NotificationsOff
        HeroPhase.FASTING_DAY -> Icons.Filled.WbSunny
        HeroPhase.IFTAR_PRE_WINDOW -> Icons.Filled.WbTwilight
        HeroPhase.IFTAR_ALARM -> Icons.Filled.NotificationsActive
        HeroPhase.IFTAR_STOPPED -> Icons.Filled.NotificationsOff
        HeroPhase.FAST_COMPLETED -> Icons.Filled.Verified
        HeroPhase.AUTO_COMPLETED -> Icons.Filled.Verified
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(modifier = Modifier.padding(22.dp)) {

            // =========================
            // TITLE + BUTTON
            // =========================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = phaseIcon(state.phase),
                        contentDescription = null,
                        tint = Color(0xFFFBBF24)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = state.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                when (state.buttonType) {
                    HeroButtonType.DONE_SEHRI -> {
                        TextButton(
                            onClick = {
                                onDoneClick(state.phase)
                            }
                        ) {
                            Text("DONE SEHRI")
                        }
                    }

                    HeroButtonType.DONE_IFTAR -> {
                        TextButton(
                            onClick = {
                                onDoneClick(state.phase)
                            }
                        ) {
                            Text("DONE IFTAR")
                        }
                    }

                    HeroButtonType.STOP_ALARM -> {
                        TextButton(
                            onClick = {
                                onStopClick(state.phase)
                            }
                        ) {
                            Text("STOP 🔔")
                        }
                    }

                    else -> {}
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // =========================
            // COUNTDOWN
            // =========================
            Text(
                text = countdown,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFBBF24)
            )

            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = Color(0xFF334155))
            Spacer(modifier = Modifier.height(16.dp))

            // =========================
            // PRIMARY TARGET
            // =========================
            InfoRow(
                icon = Icons.Filled.AccessTime,
                label = state.targetLabel,
                time = formatTo12Hour(state.targetTime)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // =========================
            // SECONDARY TARGET
            // =========================
            state.secondaryLabel?.let {
                InfoRow(
                    icon = Icons.Filled.NightsStay,
                    label = it,
                    time = formatTo12Hour(state.secondaryTime ?: "")
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // =========================
            // NEXT PRAYER
            // =========================
            InfoRow(
                icon = Icons.Filled.Mosque,
                label = "Next • ${state.nextPrayerLabel}",
                time = formatTo12Hour(state.nextPrayerTime)
            )

            Spacer(modifier = Modifier.height(18.dp))
            Divider(color = Color(0xFF334155))
            Spacer(modifier = Modifier.height(14.dp))

            // =========================
            // MESSAGE SECTION
            // =========================
            when (state.messageType) {

                MessageType.SEHRI_DUA,
                MessageType.IFTAR_DUA -> {

                    Row(verticalAlignment = Alignment.Top) {

                        Icon(
                            imageVector = Icons.Filled.MenuBook,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {

                            Text(
                                text = state.duaTitle ?: "",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = state.duaText ?: "",
                                fontSize = 13.sp,
                                color = Color(0xFFE2E8F0)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = state.duaTranslation ?: "",
                                fontSize = 12.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }

                MessageType.QUOTE -> {

                    Row(verticalAlignment = Alignment.Top) {

                        Icon(
                            imageVector = Icons.Filled.FormatQuote,
                            contentDescription = null,
                            tint = Color(0xFFE2E8F0)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = state.displayMessage,
                            fontSize = 14.sp,
                            color = Color(0xFFE2E8F0)
                        )
                    }
                }

                else -> {

                    Row(verticalAlignment = Alignment.Top) {

                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = Color(0xFFE2E8F0)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = state.displayMessage,
                            fontSize = 14.sp,
                            color = Color(0xFFE2E8F0)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    time: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.LightGray
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.LightGray
            )
        }

        Text(
            text = time,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}