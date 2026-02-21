package com.shkhan.iqra.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavController) {

    val items = listOf(
        Triple("Home", Screen.Home.route, Icons.Default.Home),
        Triple("Prayers", Screen.Prayers.route, Icons.Default.Favorite),
        Triple("Progress", Screen.Progress.route, Icons.Default.List)
    )

    NavigationBar {

        val backStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry.value?.destination?.route

        items.forEach { item ->

            NavigationBarItem(
                selected = currentRoute == item.second,
                onClick = {
                    navController.navigate(item.second) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.third,
                        contentDescription = item.first
                    )
                },
                label = {
                    Text(item.first)
                }
            )
        }
    }
}