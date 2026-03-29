package com.boomingarage.shared.data.mock

import com.boomingarage.shared.domain.model.BillingLineItem
import com.boomingarage.shared.domain.model.BillingSummary
import com.boomingarage.shared.domain.repository.PaymentRepository
import com.boomingarage.shared.util.Constants

class MockPaymentRepository : PaymentRepository {
    override suspend fun createPaymentIntent(bookingId: String, amount: Double): Result<String> {
        return Result.success("pi_mock_${bookingId}")
    }

    override suspend fun confirmPayment(paymentIntentId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getBillingSummary(bookingId: String): Result<BillingSummary> {
        // Simulate a 2-hour session at $45/hr
        val hours = 2.0
        val rate = 45.0
        val baseAmount = hours * rate
        val tax = baseAmount * Constants.TAX_RATE
        val total = baseAmount + tax

        return Result.success(
            BillingSummary(
                bookingId = bookingId,
                lineItems = listOf(
                    BillingLineItem(
                        description = "Bay rental (${hours.toInt()} hours)",
                        hours = hours,
                        ratePerHour = rate,
                        amount = baseAmount
                    )
                ),
                subtotal = baseAmount,
                tax = tax,
                total = total
            )
        )
    }
}
