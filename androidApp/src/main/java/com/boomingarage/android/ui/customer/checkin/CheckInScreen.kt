package com.boomingarage.android.ui.customer.checkin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boomingarage.shared.domain.model.BookingStatus
import com.boomingarage.shared.util.DateTimeUtil
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInScreen(
    bookingId: String,
    onSessionEnded: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: CheckInViewModel = koinViewModel { parametersOf(bookingId) }
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.sessionEnded) {
        if (uiState.sessionEnded) onSessionEnded()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Check In") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            uiState.booking?.let { booking ->
                // Booking info card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(booking.garageName, style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(4.dp))
                        Text("Bay: ${booking.bayName}", style = MaterialTheme.typography.bodyMedium)
                        Text("Date: ${DateTimeUtil.formatDate(booking.date)}", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "Time: ${DateTimeUtil.formatTime(booking.startTime)} - ${DateTimeUtil.formatTime(booking.endTime)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (!uiState.isCheckedIn && booking.status == BookingStatus.CONFIRMED) {
                    // Check-in button
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Ready to start?", style = MaterialTheme.typography.headlineMedium)
                    Text(
                        "Check in to start your session timer",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.checkIn() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !uiState.isLoading
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text("Check In Now", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                } else if (uiState.isCheckedIn) {
                    // Timer display
                    val totalSeconds = abs(uiState.remainingSeconds)
                    val hours = totalSeconds / 3600
                    val minutes = (totalSeconds % 3600) / 60
                    val seconds = totalSeconds % 60
                    val timeStr = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                    Text(
                        text = if (uiState.isOvertime) "OVERTIME" else "Time Remaining",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (uiState.isOvertime) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = timeStr,
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 64.sp),
                        color = if (uiState.isOvertime)
                            MaterialTheme.colorScheme.error
                        else if (uiState.remainingSeconds < 600) // < 10 min
                            MaterialTheme.colorScheme.tertiary
                        else
                            MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    if (uiState.isOvertime) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "You are past your scheduled time. Additional charges may apply.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Request assistance
                    OutlinedButton(
                        onClick = { viewModel.requestAssistance("Need help") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.SupportAgent, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Request Assistance")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // End session
                    Button(
                        onClick = { viewModel.endSession() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (uiState.isOvertime)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.primary
                        ),
                        enabled = !uiState.isLoading
                    ) {
                        Text("End Session", style = MaterialTheme.typography.titleMedium)
                    }
                }

                if (uiState.error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(uiState.error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
