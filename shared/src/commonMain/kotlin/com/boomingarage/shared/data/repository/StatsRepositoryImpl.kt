package com.boomingarage.shared.data.repository

import com.boomingarage.shared.data.remote.dto.BookingDto
import com.boomingarage.shared.domain.model.UtilizationStats
import com.boomingarage.shared.domain.repository.StatsRepository
import com.boomingarage.shared.util.Constants
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.datetime.*

class StatsRepositoryImpl : StatsRepository {
    private val firestore = Firebase.firestore

    override suspend fun getDailyStats(garageId: String, date: String): Result<UtilizationStats> = runCatching {
        val bookings = firestore.collection(Constants.COLLECTION_BOOKINGS)
            .where { "garageId" equalTo garageId }
            .where { "date" equalTo date }
            .get()

        val garageDoc = firestore.collection(Constants.COLLECTION_GARAGES).document(garageId).get()
        val totalBays = garageDoc.get<Int>("bayCount")

        val bookingList = bookings.documents.map { it.data<BookingDto>() }
        val revenue = bookingList.sumOf { it.hoursBooked * it.hourlyRate }
        val totalBookedHours = bookingList.sumOf { it.hoursBooked }
        val maxHours = totalBays * 12 // assuming 12 operating hours

        UtilizationStats(
            garageId = garageId,
            date = date,
            totalBays = totalBays,
            bookingsToday = bookingList.size,
            revenueToday = revenue,
            utilizationPercent = if (maxHours > 0) (totalBookedHours.toDouble() / maxHours) * 100 else 0.0,
            averageSessionHours = if (bookingList.isNotEmpty()) totalBookedHours.toDouble() / bookingList.size else 0.0
        )
    }

    override suspend fun getWeeklyStats(garageId: String, startDate: String): Result<UtilizationStats> = runCatching {
        val start = LocalDate.parse(startDate)
        val dates = (0..6).map {
            start.plus(DatePeriod(days = it)).toString()
        }

        var totalBookings = 0
        var totalRevenue = 0.0
        var totalHours = 0

        for (date in dates) {
            val daily = getDailyStats(garageId, date).getOrNull()
            if (daily != null) {
                totalBookings += daily.bookingsToday
                totalRevenue += daily.revenueToday
            }
        }

        val garageDoc = firestore.collection(Constants.COLLECTION_GARAGES).document(garageId).get()
        val totalBays = garageDoc.get<Int>("bayCount")

        UtilizationStats(
            garageId = garageId,
            date = startDate,
            totalBays = totalBays,
            bookingsToday = totalBookings,
            revenueToday = totalRevenue / 7,
            utilizationPercent = 0.0,
            weeklyRevenue = totalRevenue,
            weeklyBookings = totalBookings,
            averageSessionHours = if (totalBookings > 0) totalHours.toDouble() / totalBookings else 0.0
        )
    }

    override suspend fun getRevenueHistory(garageId: String, days: Int): Result<List<UtilizationStats>> = runCatching {
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        (0 until days).mapNotNull { offset ->
            val date = today.minus(DatePeriod(days = offset)).toString()
            getDailyStats(garageId, date).getOrNull()
        }
    }
}
