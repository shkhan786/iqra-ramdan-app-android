package com.shkhan.iqra.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Prayers : Screen("prayers")
    object Progress : Screen("progress")
}