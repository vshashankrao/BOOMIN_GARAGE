package com.boomingarage.android.ui.operator.stats

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
import com.boomingarage.shared.util.DateTimeUtil
import org.koin.androidx.compose.koinViewModel

@Composable
fun RevenueStatsScreen(
    viewModel: RevenueStatsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Revenue & Stats",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(24.dp, 16.dp)
        )

        // Time range selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(7 to "7 days", 14 to "14 days", 30 to "30 days").forEach { (days, label) ->
                FilterChip(
                    selected = uiState.selectedDays == days,
                    onClick = { viewModel.selectDays(days) },
                    label = { Text(label) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Summary card
            val totalRevenue = uiState.revenueHistory.sumOf { it.revenueToday }
            val totalBookings = uiState.revenueHistory.sumOf { it.bookingsToday }
            val avgUtil = if (uiState.revenueHistory.isNotEmpty())
                uiState.revenueHistory.map { it.utilizationPercent }.average()
            else 0.0

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Summary", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Revenue")
                        Text(
                            "$${String.format("%.2f", totalRevenue)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Bookings")
                        Text("$totalBookings", style = MaterialTheme.typography.titleMedium)
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Avg Utilization")
                        Text("${String.format("%.1f", avgUtil)}%", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Daily Breakdown",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.revenueHistory) { stat ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(DateTimeUtil.formatDate(stat.date), style = MaterialTheme.typography.titleSmall)
                                Text(
                                    "${stat.bookingsToday} bookings",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "$${String.format("%.2f", stat.revenueToday)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "${String.format("%.0f", stat.utilizationPercent)}% util",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
