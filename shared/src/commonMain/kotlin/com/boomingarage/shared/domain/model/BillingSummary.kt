package com.boomingarage.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BillingSummary(
    val bookingId: String,
    val lineItems: List<BillingLineItem> = emptyList(),
    val subtotal: Double = 0.0,
    val tax: Double = 0.0,
    val total: Double = 0.0,
    val isPaid: Boolean = false,
    val paymentIntentId: String? = null
)

@Serializable
data class BillingLineItem(
    val description: String,
    val hours: Double,
    val ratePerHour: Double,
    val amount: Double
)
