package com.boomingarage.shared.domain.repository

import com.boomingarage.shared.domain.model.Booking
import com.boomingarage.shared.domain.model.BookingStatus
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    suspend fun create(booking: Booking): Result<Booking>
    suspend fun cancel(bookingId: String): Result<Unit>
    suspend fun updateStatus(bookingId: String, status: BookingStatus): Result<Unit>
    suspend fun getById(bookingId: String): Result<Booking>
    fun getByUser(userId: String): Flow<List<Booking>>
    fun getByGarage(garageId: String, date: String? = null): Flow<List<Booking>>
    suspend fun getUpcomingByUser(userId: String): Result<List<Booking>>
    suspend fun getPastByUser(userId: String): Result<List<Booking>>
}
