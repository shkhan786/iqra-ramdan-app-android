package com.shkhan.iqra.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.shkhan.iqra.ui.DashboardScreen
import com.shkhan.iqra.ui.PrayersScreen
import com.shkhan.iqra.ui.viewmodel.PrayerViewModel

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    // ✅ One shared ViewModel
    val prayerViewModel: PrayerViewModel = viewModel()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(Screen.Home.route) {
                DashboardScreen(prayerViewModel)
            }

            composable(Screen.Prayers.route) {
                PrayersScreen(prayerViewModel)
            }

            composable(Screen.Progress.route) {
                // Later
            }
        }
    }
}