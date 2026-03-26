package com.boomingarage.android.ui.operator.bays

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
import com.boomingarage.shared.domain.model.Bay
import org.koin.androidx.compose.koinViewModel

@Composable
fun BayManagementScreen(
    viewModel: BayManagementViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Bay Management",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(24.dp, 16.dp)
        )

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.bays) { bay ->
                    BayManagementItem(
                        bay = bay,
                        onToggle = { viewModel.toggleBayAvailability(bay) }
                    )
                }
            }
        }
    }
}

@Composable
fun BayManagementItem(
    bay: Bay,
    onToggle: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(bay.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    "Bay #${bay.number}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (bay.features.isNotEmpty()) {
                    Text(
                        bay.features.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (bay.hourlyRate != null) {
                    Text(
                        "Custom rate: $${String.format("%.2f", bay.hourlyRate)}/hr",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Switch(
                    checked = bay.isAvailable,
                    onCheckedChange = { onToggle() }
                )
                Text(
                    if (bay.isAvailable) "Available" else "Unavailable",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (bay.isAvailable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
