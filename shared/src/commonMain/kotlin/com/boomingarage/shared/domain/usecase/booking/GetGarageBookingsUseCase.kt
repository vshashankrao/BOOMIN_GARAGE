package com.boomingarage.shared.domain.usecase.booking

import com.boomingarage.shared.domain.model.Booking
import com.boomingarage.shared.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow

class GetGarageBookingsUseCase(private val bookingRepository: BookingRepository) {
    operator fun invoke(garageId: String, date: String? = null): Flow<List<Booking>> {
        return bookingRepository.getByGarage(garageId, date)
    }
}
