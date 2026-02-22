package com.shkhan.iqra.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.shkhan.iqra.ui.model.FastingStatus

@Composable
fun StatusSelectionSheet(
    currentStatus: FastingStatus,
    onStatusSelected: (FastingStatus) -> Unit
) {

    Column(modifier = Modifier.padding(20.dp)) {

        Text(
            text = "Change Fasting Status",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        StatusOption(
            label = "Fasting Today",
            icon = Icons.Default.CheckCircle,
            color = Color(0xFF22C55E),
            onClick = { onStatusSelected(FastingStatus.FASTING) }
        )

        StatusOption(
            label = "Not Fasting Today",
            icon = Icons.Default.RadioButtonUnchecked,
            color = Color.Gray,
            onClick = { onStatusSelected(FastingStatus.NOT_FASTING) }
        )

        StatusOption(
            label = "Broke Fast",
            icon = Icons.Default.Error,
            color = Color(0xFFEF4444),
            onClick = { onStatusSelected(FastingStatus.BROKE_FAST) }
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun StatusOption(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(text = label)
    }
}