package com.boomingarage.shared.domain.repository

import com.boomingarage.shared.domain.model.BillingSummary

interface PaymentRepository {
    suspend fun createPaymentIntent(bookingId: String, amount: Double): Result<String> // returns clientSecret
    suspend fun confirmPayment(paymentIntentId: String): Result<Unit>
    suspend fun getBillingSummary(bookingId: String): Result<BillingSummary>
}
