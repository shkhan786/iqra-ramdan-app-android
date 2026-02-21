package com.shkhan.iqra.ui.api

data class CalendarResponse(
    val data: List<DayData>
)

data class DayData(
    val date: DateInfo,
    val timings: Timings
)

data class DateInfo(
    val gregorian: Gregorian,
    val hijri: Hijri
)

data class Gregorian(
    val date: String
)

data class Hijri(
    val day: String,
    val month: HijriMonth
)

data class HijriMonth(
    val number: Int
)

data class Timings(
    val Fajr: String,
    val Dhuhr: String,
    val Asr: String,
    val Maghrib: String,
    val Isha: String
)