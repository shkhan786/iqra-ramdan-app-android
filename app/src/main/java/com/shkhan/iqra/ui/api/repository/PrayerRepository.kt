package com.shkhan.iqra.ui.repository

import com.shkhan.iqra.ui.api.ApiClient
import java.time.LocalDate

class PrayerRepository {

    suspend fun getTodayPrayerTimes(): TimingsData? {

        val today = LocalDate.now()
        val response = ApiClient.api.getMonthlyCalendar(
            city = "Bangalore",
            country = "India",
            method = 7,
            month = today.monthValue,
            year = today.year
        )

        val todayString = today.dayOfMonth.toString().padStart(2, '0')

        val todayData = response.data.find {
            it.date.gregorian.date.startsWith(todayString)
        }

        return todayData?.let {
            TimingsData(
                fajr = it.timings.Fajr.substringBefore(" "),
                dhuhr = it.timings.Dhuhr.substringBefore(" "),
                asr = it.timings.Asr.substringBefore(" "),
                maghrib = it.timings.Maghrib.substringBefore(" "),
                isha = it.timings.Isha.substringBefore(" ")
            )
        }
    }
}

data class TimingsData(
    val fajr: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String
)