package com.boomingarage.shared.domain.usecase.checkin

import com.boomingarage.shared.domain.model.BookingStatus
import com.boomingarage.shared.domain.model.CheckInSession
import com.boomingarage.shared.domain.repository.BookingRepository
import com.boomingarage.shared.domain.repository.CheckInRepository

class EndCheckInUseCase(
    private val checkInRepository: CheckInRepository,
    private val bookingRepository: BookingRepository
) {
    suspend operator fun invoke(sessionId: String, bookingId: String): Result<CheckInSession> {
        val session = checkInRepository.endSession(sessionId).getOrElse { return Result.failure(it) }
        bookingRepository.updateStatus(bookingId, BookingStatus.COMPLETED)
        return Result.success(session)
    }
}
