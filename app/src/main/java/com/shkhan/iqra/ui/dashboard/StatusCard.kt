package com.shkhan.iqra.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shkhan.iqra.ui.model.FastingStatus

@Composable
fun StatusCard(
    status: FastingStatus,
    ramadanDay: Int,
    completedDays: Int,
    totalDays: Int,
    streak: Int,
    onChangeClick: () -> Unit
) {

    val remainingDays = totalDays - completedDays
    val progress = completedDays.toFloat() / totalDays.toFloat()

    val (statusText, statusColor, statusIcon) = when (status) {
        FastingStatus.FASTING ->
            Triple("Fasting Today", Color(0xFF22C55E), Icons.Default.CheckCircle)

        FastingStatus.BROKE_FAST ->
            Triple("Fast Broken", Color(0xFFEF4444), Icons.Default.Error)

        FastingStatus.NOT_FASTING ->
            Triple("Not Fasting Today", Color.Gray, Icons.Default.RadioButtonUnchecked)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // ===============================
            // STATUS ROW
            // ===============================
            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = statusColor
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = statusText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ===============================
            // RAMADAN DAY
            // ===============================
            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = Color.LightGray
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Ramadan Day $ramadanDay / $totalDays",
                    fontSize = 14.sp,
                    color = Color.LightGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===============================
            // COMPLETION INFO
            // ===============================
            Text(
                text = "Completed: $completedDays   •   Remaining: $remainingDays",
                fontSize = 13.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = Color(0xFFFBBF24),
                trackColor = Color(0xFF334155)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Divider(color = Color(0xFF334155))

            Spacer(modifier = Modifier.height(14.dp))

            // ===============================
            // BOTTOM ROW (STREAK + BUTTON)
            // ===============================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = "🔥",
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "$streak Day Streak",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFBBF24)
                    )
                }

                OutlinedButton(
                    onClick = onChangeClick
                ) {
                    Text("Change Status")
                }
            }
        }
    }
}