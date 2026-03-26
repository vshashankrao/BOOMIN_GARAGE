package com.boomingarage.shared.domain.usecase.booking

import com.boomingarage.shared.domain.model.Booking
import com.boomingarage.shared.domain.model.BookingStatus
import com.boomingarage.shared.domain.repository.BookingRepository
import com.boomingarage.shared.domain.repository.AuthRepository

class CreateBookingUseCase(
    private val bookingRepository: BookingRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        garageId: String,
        garageName: String,
        bayId: String,
        bayName: String,
        date: String,
        startTime: String,
        endTime: String,
        hoursBooked: Int,
        hourlyRate: Double
    ): Result<Booking> {
        val user = authRepository.getCurrentUser()
            ?: return Result.failure(IllegalStateException("User must be signed in to book"))

        val booking = Booking(
            id = "", // will be set by Firestore
            garageId = garageId,
            garageName = garageName,
            bayId = bayId,
            bayName = bayName,
            userId = user.id,
            date = date,
            startTime = startTime,
            endTime = endTime,
            hoursBooked = hoursBooked,
            hourlyRate = hourlyRate,
            status = BookingStatus.CONFIRMED,
            totalEstimate = hoursBooked * hourlyRate,
            depositAmount = hoursBooked * hourlyRate * 0.25 // 25% deposit
        )
        return bookingRepository.create(booking)
    }
}
