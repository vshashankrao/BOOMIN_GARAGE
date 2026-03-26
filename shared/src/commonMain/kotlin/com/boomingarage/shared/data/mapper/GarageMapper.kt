package com.boomingarage.shared.data.mapper

import com.boomingarage.shared.data.remote.dto.BayDto
import com.boomingarage.shared.data.remote.dto.DayHoursDto
import com.boomingarage.shared.data.remote.dto.GarageDto
import com.boomingarage.shared.domain.model.Bay
import com.boomingarage.shared.domain.model.DayHours
import com.boomingarage.shared.domain.model.Garage

object GarageMapper {
    fun toDomain(dto: GarageDto): Garage = Garage(
        id = dto.id,
        name = dto.name,
        address = dto.address,
        latitude = dto.latitude,
        longitude = dto.longitude,
        operatorId = dto.operatorId,
        photoUrls = dto.photoUrls,
        amenities = dto.amenities,
        hourlyRate = dto.hourlyRate,
        operatingHours = dto.operatingHours.mapValues { (_, v) ->
            DayHours(openTime = v.openTime, closeTime = v.closeTime, isClosed = v.isClosed)
        },
        bayCount = dto.bayCount,
        averageRating = dto.averageRating,
        totalReviews = dto.totalReviews,
        description = dto.description
    )

    fun bayToDomain(dto: BayDto): Bay = Bay(
        id = dto.id,
        garageId = dto.garageId,
        name = dto.name,
        number = dto.number,
        isAvailable = dto.isAvailable,
        features = dto.features,
        hourlyRate = dto.hourlyRate
    )

    fun bayToDto(bay: Bay): BayDto = BayDto(
        id = bay.id,
        garageId = bay.garageId,
        name = bay.name,
        number = bay.number,
        isAvailable = bay.isAvailable,
        features = bay.features,
        hourlyRate = bay.hourlyRate
    )
}
