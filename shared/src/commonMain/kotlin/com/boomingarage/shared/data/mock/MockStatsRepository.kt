package com.boomingarage.shared.data.mock

import com.boomingarage.shared.domain.model.UtilizationStats
import com.boomingarage.shared.domain.repository.StatsRepository
import kotlinx.datetime.*
import kotlin.random.Random

class MockStatsRepository : StatsRepository {
    override suspend fun getDailyStats(garageId: String, date: String): Result<UtilizationStats> {
        // Generate plausible random stats for each day
        val seed = date.hashCode()
        val rng = Random(seed)
        val bookings = rng.nextInt(3, 12)
        val avgHours = 1.5 + rng.nextDouble() * 2.0
        val rate = 45.0
        val revenue = bookings * avgHours * rate
        val totalBays = 4
        val utilization = (bookings * avgHours) / (totalBays * 12) * 100

        return Result.success(
            UtilizationStats(
                garageId = garageId,
                date = date,
                totalBays = totalBays,
                bookingsToday = bookings,
                revenueToday = revenue,
                utilizationPercent = utilization.coerceIn(0.0, 100.0),
                averageSessionHours = avgHours
            )
        )
    }

    override suspend fun getWeeklyStats(garageId: String, startDate: String): Result<UtilizationStats> {
        val start = LocalDate.parse(startDate)
        var totalBookings = 0
        var totalRevenue = 0.0
        var totalHours = 0.0

        for (i in 0..6) {
            val date = start.plus(DatePeriod(days = i)).toString()
            val daily = getDailyStats(garageId, date).getOrNull() ?: continue
            totalBookings += daily.bookingsToday
            totalRevenue += daily.revenueToday
            totalHours += daily.bookingsToday * daily.averageSessionHours
        }

        return Result.success(
            UtilizationStats(
                garageId = garageId,
                date = startDate,
                totalBays = 4,
                bookingsToday = totalBookings / 7,
                revenueToday = totalRevenue / 7,
                utilizationPercent = 0.0,
                weeklyRevenue = totalRevenue,
                weeklyBookings = totalBookings,
                averageSessionHours = if (totalBookings > 0) totalHours / totalBookings else 0.0
            )
        )
    }

    override suspend fun getRevenueHistory(garageId: String, days: Int): Result<List<UtilizationStats>> {
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        val history = (0 until days).mapNotNull { offset ->
            val date = today.minus(DatePeriod(days = offset)).toString()
            getDailyStats(garageId, date).getOrNull()
        }
        return Result.success(history)
    }
}
