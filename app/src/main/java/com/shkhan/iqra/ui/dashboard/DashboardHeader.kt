package com.shkhan.iqra.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardHeader() {

    // 🔹 Live Clock
    val currentTime by produceState(initialValue = Date()) {
        while (true) {
            value = Date()
            delay(1000)
        }
    }

    val dateFormatter = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

    val formattedDate = dateFormatter.format(currentTime)
    val formattedTime = timeFormatter.format(currentTime)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        // =========================
        // TITLE + LIVE TIME ROW
        // =========================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = Icons.Filled.DarkMode,
                    contentDescription = null,
                    tint = Color(0xFFFBBF24)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Iqra",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = null,
                    tint = Color.LightGray
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = formattedTime,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // =========================
        // DATE ROW
        // =========================
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = null,
                tint = Color.LightGray
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = formattedDate,
                fontSize = 14.sp,
                color = Color.LightGray
            )
        }
    }
}