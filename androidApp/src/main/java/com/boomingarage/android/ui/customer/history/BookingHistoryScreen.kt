package com.boomingarage.android.ui.customer.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boomingarage.shared.domain.model.Booking
import com.boomingarage.shared.domain.model.BookingStatus
import com.boomingarage.shared.util.DateTimeUtil
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookingHistoryScreen(
    onBookingClick: (String) -> Unit,
    onWriteReview: (String, String) -> Unit,
    viewModel: BookingHistoryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "My Bookings",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(24.dp, 16.dp)
        )

        TabRow(selectedTabIndex = uiState.selectedTab) {
            Tab(
                selected = uiState.selectedTab == 0,
                onClick = { viewModel.selectTab(0) },
                text = { Text("Upcoming") }
            )
            Tab(
                selected = uiState.selectedTab == 1,
                onClick = { viewModel.selectTab(1) },
                text = { Text("Past") }
            )
        }

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val bookings = if (uiState.selectedTab == 0) uiState.upcomingBookings else uiState.pastBookings

            if (bookings.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        if (uiState.selectedTab == 0) "No upcoming bookings" else "No past bookings",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(bookings) { booking ->
                        BookingHistoryItem(
                            booking = booking,
                            onClick = {
                                if (booking.status == BookingStatus.CONFIRMED) {
                                    onBookingClick(booking.id)
                                }
                            },
                            onReview = {
                                if (booking.status == BookingStatus.COMPLETED) {
                                    onWriteReview(booking.garageId, booking.id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookingHistoryItem(
    booking: Booking,
    onClick: () -> Unit,
    onReview: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(booking.garageName, style = MaterialTheme.typography.titleMedium)
                StatusBadge(booking.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Bay: ${booking.bayName}", style = MaterialTheme.typography.bodyMedium)
            Text(
                "${DateTimeUtil.formatDate(booking.date)} | ${DateTimeUtil.formatTime(booking.startTime)} - ${DateTimeUtil.formatTime(booking.endTime)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Estimated: $${String.format("%.2f", booking.totalEstimate)}",
                style = MaterialTheme.typography.bodySmall
            )

            if (booking.status == BookingStatus.CONFIRMED) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
                    Text("Check In")
                }
            }

            if (booking.status == BookingStatus.COMPLETED) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = onReview, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Leave Review")
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: BookingStatus) {
    val (color, text) = when (status) {
        BookingStatus.PENDING -> MaterialTheme.colorScheme.tertiary to "Pending"
        BookingStatus.CONFIRMED -> MaterialTheme.colorScheme.primary to "Confirmed"
        BookingStatus.CHECKED_IN -> MaterialTheme.colorScheme.secondary to "In Progress"
        BookingStatus.COMPLETED -> MaterialTheme.colorScheme.outline to "Completed"
        BookingStatus.CANCELLED -> MaterialTheme.colorScheme.error to "Cancelled"
    }
    Surface(color = color.copy(alpha = 0.15f), shape = MaterialTheme.shapes.small) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
