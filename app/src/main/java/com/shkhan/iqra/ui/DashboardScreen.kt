package com.shkhan.iqra.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shkhan.iqra.ui.repository.QuoteRepository
import com.shkhan.iqra.ui.viewmodel.PrayerViewModel
import kotlinx.coroutines.delay
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

private val BackgroundGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF0B132B), Color(0xFF16213E))
)

@Composable
fun DashboardScreen(viewModel: PrayerViewModel) {

    val timings by viewModel.timings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var isFasting by remember { mutableStateOf(true) }
    var now by remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            now = LocalDateTime.now()
            delay(1000)
        }
    }

    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGradient)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 30.dp)
        ) {

            // HEADER
            Text(
                text = "Iqra 🌙",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC857)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Ramadan Mubarak",
                fontSize = 15.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${now.format(dateFormatter)} • ${now.format(timeFormatter)}",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.75f)
            )

            Spacer(modifier = Modifier.height(28.dp))

            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFFFFC857))
            } else if (timings != null) {

                RamadanStatusCard(
                    maghrib = timings!!.maghrib,
                    isFasting = isFasting,
                    onToggle = { isFasting = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrayerTimeRow(
                    sehri = timings!!.fajr,
                    iftar = timings!!.maghrib
                )

                Spacer(modifier = Modifier.height(24.dp))

                QuoteCard()
            }
        }
    }
}

@Composable
fun RamadanStatusCard(
    maghrib: String,
    isFasting: Boolean,
    onToggle: (Boolean) -> Unit
) {

    var countdown by remember { mutableStateOf("") }

    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())

    LaunchedEffect(maghrib) {
        while (true) {
            val now = LocalTime.now()
            val target = LocalTime.parse(maghrib)

            countdown = if (now.isBefore(target)) {
                val duration = Duration.between(now, target)
                String.format(
                    Locale.getDefault(),
                    "%02d:%02d:%02d",
                    duration.toHours(),
                    duration.toMinutes() % 60,
                    duration.seconds % 60
                )
            } else {
                "Iftar Time 🌙"
            }
            delay(1000)
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF22304A)),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {

            Text(
                text = "Iftar in",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = countdown,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC857)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Maghrib • ${
                    LocalTime.parse(maghrib).format(timeFormatter)
                }",
                fontSize = 13.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Divider(color = Color.White.copy(alpha = 0.1f))

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Column {
                    Text(
                        text = if (isFasting) "Fasting Today" else "Not Fasting Today",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Text(
                        text = if (isFasting) "May Allah accept it"
                        else "You can make intention",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                }

                Switch(
                    checked = isFasting,
                    onCheckedChange = onToggle
                )
            }
        }
    }
}

@Composable
fun PrayerTimeRow(sehri: String, iftar: String) {

    val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        InfoCard(
            title = "Sehri Ends",
            time = LocalTime.parse(sehri).format(formatter),
            modifier = Modifier.weight(1f)
        )

        InfoCard(
            title = "Iftar",
            time = LocalTime.parse(iftar).format(formatter),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun InfoCard(title: String, time: String, modifier: Modifier) {

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 22.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = title,
                fontSize = 13.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = time,
                fontSize = 19.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

//@Composable
//fun QuoteCard() {
//
//    val quotes = listOf(
//        "Indeed, Allah is with the patient.",
//        "Ramadan is the month of mercy.",
//        "Every good deed is multiplied in Ramadan.",
//        "The best among you are those who learn the Quran."
//    )
//
//    val randomQuote = remember { quotes[Random.nextInt(quotes.size)] }
//
//    Card(
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B)),
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Text(
//            text = randomQuote,
//            fontSize = 14.sp,
//            modifier = Modifier.padding(22.dp),
//            color = Color.White
//        )
//    }
//}

//@Composable
//fun QuoteCard() {
//
//    val context = LocalContext.current
//
//    val quote = remember {
//        QuoteRepository(context).getRandomQuote()
//    }
//
//    Card(
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B)),
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Text(
//            text = quote,
//            fontSize = 14.sp,
//            modifier = Modifier.padding(22.dp),
//            color = Color.White
//        )
//    }
//}

@Composable
fun QuoteCard() {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var quote by remember { mutableStateOf("") }

    val repository = remember {
        QuoteRepository(context)
    }

    // Load first quote
    LaunchedEffect(Unit) {
        quote = repository.getRandomQuote()
    }

    // Observe lifecycle to change quote on resume
    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                quote = repository.getRandomQuote()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = quote,
            fontSize = 14.sp,
            modifier = Modifier.padding(22.dp),
            color = Color.White
        )
    }
}