package com.boomingarage.shared.domain.usecase.booking

import com.boomingarage.shared.domain.model.BookingStatus
import com.boomingarage.shared.domain.repository.BookingRepository

class CancelBookingUseCase(private val bookingRepository: BookingRepository) {
    suspend operator fun invoke(bookingId: String): Result<Unit> {
        val booking = bookingRepository.getById(bookingId).getOrElse { return Result.failure(it) }
        if (booking.status == BookingStatus.CHECKED_IN) {
            return Result.failure(IllegalStateException("Cannot cancel a booking that is already checked in"))
        }
        if (booking.status == BookingStatus.COMPLETED) {
            return Result.failure(IllegalStateException("Cannot cancel a completed booking"))
        }
        return bookingRepository.cancel(bookingId)
    }
}
