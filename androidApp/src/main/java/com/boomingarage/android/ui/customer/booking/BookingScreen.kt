package com.boomingarage.android.ui.customer.booking

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boomingarage.shared.domain.model.TimeSlot
import com.boomingarage.shared.util.DateTimeUtil
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.plus
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    garageId: String,
    onBookingConfirmed: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: BookingViewModel = koinViewModel { parametersOf(garageId) }
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.bookingId) {
        uiState.bookingId?.let { onBookingConfirmed(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book a Bay") },
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Date selection (next 7 days)
            Text("Select Date", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            val today = DateTimeUtil.today()
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items((0..6).toList()) { offset ->
                    val date = today.plus(kotlinx.datetime.DatePeriod(days = offset))
                    val dateStr = date.toString()
                    val isSelected = uiState.selectedDate == dateStr
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectDate(dateStr) },
                        label = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(date.dayOfWeek.name.take(3))
                                Text(date.dayOfMonth.toString(), style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bay selection
            Text("Select Bay", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.bayAvailabilities) { bayAvail ->
                        val isSelected = uiState.selectedBay?.id == bayAvail.bay.id
                        val hasSlots = bayAvail.timeSlots.any { it.isAvailable }
                        Card(
                            modifier = Modifier
                                .width(120.dp)
                                .then(
                                    if (isSelected) Modifier.border(
                                        2.dp,
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.shapes.medium
                                    ) else Modifier
                                )
                                .clickable(enabled = hasSlots) { viewModel.selectBay(bayAvail.bay) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (!hasSlots)
                                    MaterialTheme.colorScheme.surfaceVariant
                                else if (isSelected)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(bayAvail.bay.name, style = MaterialTheme.typography.titleSmall)
                                Text(
                                    if (hasSlots) "Available" else "Full",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (hasSlots) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            // Time slot selection
            if (uiState.selectedBay != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Select Start Time", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                val availableSlots = uiState.availableTimeSlots.filter { it.isAvailable }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.heightIn(max = 200.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableSlots) { slot ->
                        val isSelected = uiState.selectedStartTime == slot.startTime
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectStartTime(slot.startTime) },
                            label = { Text(DateTimeUtil.formatTime(slot.startTime), style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                // Hours selection
                Spacer(modifier = Modifier.height(24.dp))
                Text("How many hours?", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    (1..6).forEach { hours ->
                        FilterChip(
                            selected = uiState.selectedHours == hours,
                            onClick = { viewModel.selectHours(hours) },
                            label = { Text("${hours}h") }
                        )
                    }
                }
            }

            // Price summary
            if (uiState.selectedBay != null && uiState.selectedStartTime != null) {
                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Booking Summary", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        val rate = uiState.selectedBay!!.hourlyRate ?: uiState.garage?.hourlyRate ?: 0.0
                        val total = rate * uiState.selectedHours
                        val deposit = total * 0.25

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Date")
                            Text(DateTimeUtil.formatDate(uiState.selectedDate))
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Time")
                            Text("${DateTimeUtil.formatTime(uiState.selectedStartTime!!)} (${uiState.selectedHours}h)")
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Bay")
                            Text(uiState.selectedBay!!.name)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Estimated Total", style = MaterialTheme.typography.titleMedium)
                            Text("$${String.format("%.2f", total)}", style = MaterialTheme.typography.titleMedium)
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Deposit (25%)", style = MaterialTheme.typography.bodySmall)
                            Text("$${String.format("%.2f", deposit)}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.confirmBooking() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !uiState.isBooking
                ) {
                    if (uiState.isBooking) {
                        CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Confirm Booking", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(uiState.error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun BookingConfirmationScreen(
    bookingId: String,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Booking Confirmed!", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Your bay has been reserved. You'll receive a confirmation notification.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Booking ID: $bookingId",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("View My Bookings", style = MaterialTheme.typography.titleMedium)
        }
    }
}

// CheckCircle icon used from material-icons-extended
