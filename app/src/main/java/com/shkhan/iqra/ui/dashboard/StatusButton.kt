package com.shkhan.iqra.ui.dashboard

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun StatusButton(
    label: String,
    selected: Boolean
) {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFFFBBF24) else Color(0xFF334155)
        )
    ) {
        Text(
            text = label,
            color = if (selected) Color.Black else Color.White,
            fontSize = 12.sp
        )
    }
}