package com.boomingarage.shared.domain.usecase.payment

import com.boomingarage.shared.domain.model.BillingSummary
import com.boomingarage.shared.domain.repository.PaymentRepository

class ProcessPaymentUseCase(private val paymentRepository: PaymentRepository) {
    suspend fun getBillingSummary(bookingId: String): Result<BillingSummary> {
        return paymentRepository.getBillingSummary(bookingId)
    }

    suspend fun initiatePayment(bookingId: String, amount: Double): Result<String> {
        return paymentRepository.createPaymentIntent(bookingId, amount)
    }

    suspend fun confirmPayment(paymentIntentId: String): Result<Unit> {
        return paymentRepository.confirmPayment(paymentIntentId)
    }
}
