package com.shkhan.iqra.ui.model

data class Dua(
    val id: Int,
    val type: String,
    val title: String,
    val arabic: String,
    val transliteration: String,
    val translation: String
)