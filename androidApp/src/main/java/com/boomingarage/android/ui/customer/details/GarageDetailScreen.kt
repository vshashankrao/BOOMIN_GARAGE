package com.boomingarage.android.ui.customer.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boomingarage.shared.domain.model.Bay
import com.boomingarage.shared.domain.model.Review
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GarageDetailScreen(
    garageId: String,
    onBookClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: GarageDetailViewModel = koinViewModel { parametersOf(garageId) }
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.garage?.name ?: "Garage Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.garage != null) {
                ExtendedFloatingActionButton(
                    onClick = onBookClick,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Book Now")
                }
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(uiState.error ?: "Error loading details", color = MaterialTheme.colorScheme.error)
            }
        } else {
            uiState.garage?.let { garage ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                ) {
                    // Garage info header
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    "${String.format("%.1f", garage.averageRating)} (${garage.totalReviews} reviews)",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.medium) {
                            Text(
                                "$${String.format("%.0f", garage.hourlyRate)}/hr",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.width(4.dp))
                        Text(garage.address, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    if (garage.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(garage.description, style = MaterialTheme.typography.bodyLarge)
                    }

                    // Amenities
                    if (garage.amenities.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Amenities", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(garage.amenities) { amenity ->
                                AssistChip(
                                    onClick = {},
                                    label = { Text(amenity) },
                                    leadingIcon = { Icon(Icons.Default.Check, null, Modifier.size(16.dp)) }
                                )
                            }
                        }
                    }

                    // Bays section
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Service Bays (${uiState.bays.size})", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(uiState.bays) { bay ->
                            BayCard(bay)
                        }
                    }

                    // Reviews section
                    if (uiState.reviews.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Reviews", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        uiState.reviews.take(5).forEach { review ->
                            ReviewItem(review)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
                }
            }
        }
    }
}

@Composable
private fun BayCard(bay: Bay) {
    Card(
        modifier = Modifier.width(160.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (bay.isAvailable)
                MaterialTheme.colorScheme.surface
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(bay.name, style = MaterialTheme.typography.titleMedium)
                Surface(
                    color = if (bay.isAvailable)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        if (bay.isAvailable) "Open" else "In Use",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            if (bay.features.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    bay.features.joinToString(", "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ReviewItem(review: Review) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(review.userName, style = MaterialTheme.typography.titleSmall)
                Row {
                    repeat(review.rating) {
                        Icon(Icons.Default.Star, null, Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                    }
                    repeat(5 - review.rating) {
                        Icon(Icons.Default.StarBorder, null, Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            if (review.comment.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(review.comment, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
