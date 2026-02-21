package com.shkhan.iqra.ui.model

data class RamadanDay(
    val date: String,
    val ramadanDay: Int,
    val sehriEnds: String,
    val fajr: String,
    val iftar: String
)


data class Quote(
    val text: String
)