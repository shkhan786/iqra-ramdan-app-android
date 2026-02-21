package com.shkhan.iqra.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shkhan.iqra.ui.viewmodel.PrayerViewModel

@Composable
fun PrayersScreen(viewModel: PrayerViewModel) {

    val timings by viewModel.timings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0F172A)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            Text(
                text = "Today's Prayers",
                fontSize = 24.sp,
                color = Color(0xFFFBBF24)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFFFBBF24))
            } else if (timings != null) {

                PrayerRow("Fajr", timings!!.fajr)
                PrayerRow("Dhuhr", timings!!.dhuhr)
                PrayerRow("Asr", timings!!.asr)
                PrayerRow("Maghrib", timings!!.maghrib)
                PrayerRow("Isha", timings!!.isha)

            } else {
                Text("Failed to load prayer timings", color = Color.Red)
            }
        }
    }
}

@Composable
fun PrayerRow(name: String, time: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, color = Color.White, fontSize = 18.sp)
            Text(time, color = Color.White, fontSize = 18.sp)
        }
    }
}