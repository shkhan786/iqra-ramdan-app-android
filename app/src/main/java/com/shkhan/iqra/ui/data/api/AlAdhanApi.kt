package com.shkhan.iqra.ui.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface AlAdhanApi {

    @GET("v1/calendarByCity")
    suspend fun getMonthlyCalendar(
        @Query("city") city: String,
        @Query("country") country: String,
        @Query("method") method: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): CalendarResponse
}