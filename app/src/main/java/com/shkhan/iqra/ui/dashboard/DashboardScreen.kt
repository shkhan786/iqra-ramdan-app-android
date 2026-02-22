package com.shkhan.iqra.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shkhan.iqra.ui.viewmodel.PrayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    prayerViewModel: PrayerViewModel
) {

    // ===============================
    // Collect ViewModel State
    // ===============================
    val heroState by prayerViewModel.heroState.collectAsState()
    val status by prayerViewModel.fastingStatus.collectAsState()
    val completed by prayerViewModel.completedDays.collectAsState()
    val streak by prayerViewModel.streak.collectAsState()

    var showSheet by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0F172A)
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            // ==========================================
            // Scrollable Main Content
            // ==========================================
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
                    .fillMaxSize()
            ) {

                // 🔹 Header
                DashboardHeader()

                Spacer(modifier = Modifier.height(8.dp))

                // 🔹 Hero Card
                heroState?.let {
                    HeroCard(it)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 🔹 Fasting Status Card (Updated with Streak)
                StatusCard(
                    status = status,
                    ramadanDay = 4, // TODO: connect Hijri day from API
                    completedDays = completed,
                    totalDays = prayerViewModel.totalDays,
                    streak = streak,
                    onChangeClick = { showSheet = true }
                )

//                Spacer(modifier = Modifier.height(8.dp))
//
//                // 🔹 Quote Card
//                QuoteCard()

                Spacer(modifier = Modifier.height(40.dp))
            }

            // ==========================================
            // Bottom Sheet Overlay
            // ==========================================
            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showSheet = false }
                ) {
                    StatusSelectionSheet(
                        currentStatus = status,
                        onStatusSelected = { selected ->
                            prayerViewModel.updateFastingStatus(selected)
                            showSheet = false
                        }
                    )
                }
            }
        }
    }
}