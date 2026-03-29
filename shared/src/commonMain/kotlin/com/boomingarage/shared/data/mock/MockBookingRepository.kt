package com.boomingarage.shared.data.mock

import com.boomingarage.shared.domain.model.Booking
import com.boomingarage.shared.domain.model.BookingStatus
import com.boomingarage.shared.domain.repository.BookingRepository
import com.boomingarage.shared.util.DateTimeUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MockBookingRepository : BookingRepository {
    private val bookings = MutableStateFlow<List<Booking>>(emptyList())
    private var nextId = 1

    override suspend fun create(booking: Booking): Result<Booking> {
        val created = booking.copy(
            id = "booking-${nextId++}",
            createdAt = DateTimeUtil.toEpochMillis(DateTimeUtil.now())
        )
        bookings.value = bookings.value + created
        return Result.success(created)
    }

    override suspend fun cancel(bookingId: String): Result<Unit> {
        bookings.value = bookings.value.map {
            if (it.id == bookingId) it.copy(status = BookingStatus.CANCELLED) else it
        }
        return Result.success(Unit)
    }

    override suspend fun updateStatus(bookingId: String, status: BookingStatus): Result<Unit> {
        bookings.value = bookings.value.map {
            if (it.id == bookingId) it.copy(status = status) else it
        }
        return Result.success(Unit)
    }

    override suspend fun getById(bookingId: String): Result<Booking> {
        val booking = bookings.value.find { it.id == bookingId }
            ?: return Result.failure(Exception("Booking not found"))
        return Result.success(booking)
    }

    override fun getByUser(userId: String): Flow<List<Booking>> {
        return bookings.map { list ->
            list.filter { it.userId == userId }.sortedByDescending { it.createdAt }
        }
    }

    override fun getByGarage(garageId: String, date: String?): Flow<List<Booking>> {
        return bookings.map { list ->
            list.filter { it.garageId == garageId }
                .let { if (date != null) it.filter { b -> b.date == date } else it }
                .sortedBy { it.date + it.startTime }
        }
    }

    override suspend fun getUpcomingByUser(userId: String): Result<List<Booking>> {
        val today = DateTimeUtil.today().toString()
        val upcoming = bookings.value.filter {
            it.userId == userId && it.date >= today &&
            it.status in listOf(BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.CHECKED_IN)
        }.sortedBy { it.date + it.startTime }
        return Result.success(upcoming)
    }

    override suspend fun getPastByUser(userId: String): Result<List<Booking>> {
        val today = DateTimeUtil.today().toString()
        val past = bookings.value.filter {
            it.userId == userId &&
            (it.date < today || it.status in listOf(BookingStatus.COMPLETED, BookingStatus.CANCELLED))
        }.sortedByDescending { it.date + it.startTime }
        return Result.success(past)
    }
}
