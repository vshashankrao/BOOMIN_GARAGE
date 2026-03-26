package com.boomingarage.shared.domain.usecase.booking

import com.boomingarage.shared.domain.model.Booking
import com.boomingarage.shared.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow

class GetUserBookingsUseCase(private val bookingRepository: BookingRepository) {
    operator fun invoke(userId: String): Flow<List<Booking>> = bookingRepository.getByUser(userId)
    suspend fun getUpcoming(userId: String): Result<List<Booking>> = bookingRepository.getUpcomingByUser(userId)
    suspend fun getPast(userId: String): Result<List<Booking>> = bookingRepository.getPastByUser(userId)
}
