package com.boomingarage.android.ui.operator.bookings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boomingarage.android.ui.customer.history.StatusBadge
import com.boomingarage.shared.domain.model.Booking
import com.boomingarage.shared.util.DateTimeUtil
import org.koin.androidx.compose.koinViewModel

@Composable
fun ManageBookingsScreen(
    viewModel: ManageBookingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Manage Bookings",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(24.dp, 16.dp)
        )

        TabRow(selectedTabIndex = uiState.selectedTab) {
            Tab(selected = uiState.selectedTab == 0, onClick = { viewModel.selectTab(0) }, text = { Text("Today") })
            Tab(selected = uiState.selectedTab == 1, onClick = { viewModel.selectTab(1) }, text = { Text("Upcoming") })
            Tab(selected = uiState.selectedTab == 2, onClick = { viewModel.selectTab(2) }, text = { Text("All") })
        }

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val today = com.boomingarage.shared.util.DateTimeUtil.today().toString()
            val filtered = when (uiState.selectedTab) {
                0 -> uiState.bookings.filter { it.date == today }
                1 -> uiState.bookings.filter { it.date >= today }
                else -> uiState.bookings
            }

            if (filtered.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No bookings found", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filtered) { booking ->
                        OperatorBookingItem(booking)
                    }
                }
            }
        }
    }
}

@Composable
fun OperatorBookingItem(booking: Booking) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Bay: ${booking.bayName}", style = MaterialTheme.typography.titleMedium)
                StatusBadge(booking.status)
            }
            Spacer(Modifier.height(4.dp))
            Text(
                "${DateTimeUtil.formatDate(booking.date)} | ${DateTimeUtil.formatTime(booking.startTime)} - ${DateTimeUtil.formatTime(booking.endTime)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "${booking.hoursBooked}h - $${String.format("%.2f", booking.totalEstimate)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
