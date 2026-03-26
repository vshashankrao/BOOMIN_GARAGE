package com.boomingarage.shared.data.mapper

import com.boomingarage.shared.data.remote.dto.BookingDto
import com.boomingarage.shared.domain.model.Booking
import com.boomingarage.shared.domain.model.BookingStatus

object BookingMapper {
    fun toDomain(dto: BookingDto): Booking = Booking(
        id = dto.id,
        garageId = dto.garageId,
        garageName = dto.garageName,
        bayId = dto.bayId,
        bayName = dto.bayName,
        userId = dto.userId,
        date = dto.date,
        startTime = dto.startTime,
        endTime = dto.endTime,
        hoursBooked = dto.hoursBooked,
        hourlyRate = dto.hourlyRate,
        status = try { BookingStatus.valueOf(dto.status) } catch (_: Exception) { BookingStatus.PENDING },
        depositAmount = dto.depositAmount,
        totalEstimate = dto.totalEstimate,
        actualTotal = dto.actualTotal,
        createdAt = dto.createdAt
    )

    fun toDto(booking: Booking): BookingDto = BookingDto(
        id = booking.id,
        garageId = booking.garageId,
        garageName = booking.garageName,
        bayId = booking.bayId,
        bayName = booking.bayName,
        userId = booking.userId,
        date = booking.date,
        startTime = booking.startTime,
        endTime = booking.endTime,
        hoursBooked = booking.hoursBooked,
        hourlyRate = booking.hourlyRate,
        status = booking.status.name,
        depositAmount = booking.depositAmount,
        totalEstimate = booking.totalEstimate,
        actualTotal = booking.actualTotal,
        createdAt = booking.createdAt
    )
}
