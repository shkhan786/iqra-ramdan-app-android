package com.shkhan.iqra.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shkhan.iqra.ui.repository.PrayerRepository
import com.shkhan.iqra.ui.repository.TimingsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PrayerViewModel : ViewModel() {

    private val repository = PrayerRepository()

    private val _timings = MutableStateFlow<TimingsData?>(null)
    val timings: StateFlow<TimingsData?> = _timings

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadPrayerTimes()
    }

    private fun loadPrayerTimes() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _timings.value = repository.getTodayPrayerTimes()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}