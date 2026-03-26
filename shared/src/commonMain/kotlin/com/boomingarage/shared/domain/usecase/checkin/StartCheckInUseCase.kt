package com.boomingarage.shared.domain.usecase.checkin

import com.boomingarage.shared.domain.model.BookingStatus
import com.boomingarage.shared.domain.model.CheckInSession
import com.boomingarage.shared.domain.repository.BookingRepository
import com.boomingarage.shared.domain.repository.CheckInRepository

class StartCheckInUseCase(
    private val checkInRepository: CheckInRepository,
    private val bookingRepository: BookingRepository
) {
    suspend operator fun invoke(bookingId: String): Result<CheckInSession> {
        val booking = bookingRepository.getById(bookingId).getOrElse { return Result.failure(it) }
        if (booking.status != BookingStatus.CONFIRMED) {
            return Result.failure(IllegalStateException("Booking must be confirmed to check in"))
        }
        bookingRepository.updateStatus(bookingId, BookingStatus.CHECKED_IN)
        return checkInRepository.startSession(bookingId)
    }
}
