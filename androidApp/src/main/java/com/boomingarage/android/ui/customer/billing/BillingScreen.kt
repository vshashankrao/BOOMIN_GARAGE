package com.boomingarage.android.ui.customer.billing

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BillingScreen(
    bookingId: String,
    onPaymentComplete: () -> Unit,
    onWriteReview: (String) -> Unit,
    viewModel: BillingViewModel = koinViewModel { parametersOf(bookingId) }
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Billing Summary", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            uiState.summary?.let { summary ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        summary.lineItems.forEach { item ->
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.description, style = MaterialTheme.typography.bodyMedium)
                                Text("$${String.format("%.2f", item.amount)}", style = MaterialTheme.typography.bodyMedium)
                            }
                            Text(
                                "${item.hours}h x $${String.format("%.2f", item.ratePerHour)}/hr",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal")
                            Text("$${String.format("%.2f", summary.subtotal)}")
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Tax")
                            Text("$${String.format("%.2f", summary.tax)}")
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", style = MaterialTheme.typography.titleLarge)
                            Text(
                                "$${String.format("%.2f", summary.total)}",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (uiState.isPaid) {
                    Text(
                        "Payment successful!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { onWriteReview("") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Leave a Review")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onPaymentComplete,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Done", style = MaterialTheme.typography.titleMedium)
                    }
                } else {
                    Button(
                        onClick = { viewModel.pay() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !uiState.isPaying
                    ) {
                        if (uiState.isPaying) {
                            CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text("Pay $${String.format("%.2f", summary.total)}", style = MaterialTheme.typography.titleMedium)
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
}
