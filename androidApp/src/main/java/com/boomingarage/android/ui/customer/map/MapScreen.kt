package com.boomingarage.android.ui.customer.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(
    onGarageClick: (String) -> Unit,
    viewModel: MapViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val defaultLocation = LatLng(37.7749, -122.4194) // San Francisco
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        ) {
            uiState.garages.forEach { garage ->
                Marker(
                    state = MarkerState(position = LatLng(garage.latitude, garage.longitude)),
                    title = garage.name,
                    snippet = "$${String.format("%.0f", garage.hourlyRate)}/hr",
                    onClick = {
                        viewModel.selectGarage(garage)
                        false
                    }
                )
            }
        }

        // Bottom sheet preview when a garage is selected
        uiState.selectedGarage?.let { garage ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { onGarageClick(garage.id) },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(garage.name, style = MaterialTheme.typography.titleLarge)
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                "$${String.format("%.0f", garage.hourlyRate)}/hr",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.width(4.dp))
                        Text(garage.address, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(4.dp))
                        Text("${String.format("%.1f", garage.averageRating)} - ${garage.bayCount} bays available", style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { onGarageClick(garage.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View Details")
                    }
                }
            }
        }
    }
}
