package com.boomingarage.android.ui.customer.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteReviewScreen(
    garageId: String,
    bookingId: String,
    onReviewSubmitted: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: WriteReviewViewModel = koinViewModel { parametersOf(garageId, bookingId) }
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSubmitted) {
        if (uiState.isSubmitted) onReviewSubmitted()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Write a Review") },
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
            Text("How was your experience?", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(24.dp))

            // Star rating
            Row(horizontalArrangement = Arrangement.Center) {
                (1..5).forEach { star ->
                    Icon(
                        imageVector = if (star <= uiState.rating) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Star $star",
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { viewModel.setRating(star) },
                        tint = if (star <= uiState.rating)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.comment,
                onValueChange = { viewModel.setComment(it) },
                label = { Text("Your review (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 6
            )

            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(uiState.error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.submit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isSubmitting && uiState.rating > 0
            ) {
                if (uiState.isSubmitting) {
                    CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Submit Review", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
