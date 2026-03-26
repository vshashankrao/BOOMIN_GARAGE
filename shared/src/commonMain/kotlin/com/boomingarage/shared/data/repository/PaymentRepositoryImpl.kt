package com.boomingarage.shared.data.repository

import com.boomingarage.shared.domain.model.BillingLineItem
import com.boomingarage.shared.domain.model.BillingSummary
import com.boomingarage.shared.domain.repository.PaymentRepository
import com.boomingarage.shared.util.Constants
import com.boomingarage.shared.util.DateTimeUtil
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class PaymentRepositoryImpl : PaymentRepository {
    private val firestore = Firebase.firestore

    override suspend fun createPaymentIntent(bookingId: String, amount: Double): Result<String> = runCatching {
        // In production, this calls a Firebase Cloud Function that creates a Stripe PaymentIntent
        // For MVP, we simulate this by storing a payment record
        val paymentDoc = firestore.collection(Constants.COLLECTION_PAYMENTS).add(
            mapOf(
                "bookingId" to bookingId,
                "amount" to amount,
                "status" to "PENDING",
                "createdAt" to DateTimeUtil.toEpochMillis(DateTimeUtil.now())
            )
        )
        // Return the document ID as a placeholder for clientSecret
        paymentDoc.id
    }

    override suspend fun confirmPayment(paymentIntentId: String): Result<Unit> = runCatching {
        firestore.collection(Constants.COLLECTION_PAYMENTS)
            .document(paymentIntentId)
            .update(mapOf("status" to "COMPLETED"))
    }

    override suspend fun getBillingSummary(bookingId: String): Result<BillingSummary> = runCatching {
        val bookingDoc = firestore.collection(Constants.COLLECTION_BOOKINGS).document(bookingId).get()
        val hoursBooked = bookingDoc.get<Int>("hoursBooked")
        val hourlyRate = bookingDoc.get<Double>("hourlyRate")

        val baseAmount = hoursBooked * hourlyRate
        val tax = baseAmount * Constants.TAX_RATE
        val total = baseAmount + tax

        BillingSummary(
            bookingId = bookingId,
            lineItems = listOf(
                BillingLineItem(
                    description = "Bay rental ($hoursBooked hours)",
                    hours = hoursBooked.toDouble(),
                    ratePerHour = hourlyRate,
                    amount = baseAmount
                )
            ),
            subtotal = baseAmount,
            tax = tax,
            total = total
        )
    }
}
