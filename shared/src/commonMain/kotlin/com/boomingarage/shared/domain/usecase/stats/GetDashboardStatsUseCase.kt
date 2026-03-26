package com.boomingarage.shared.domain.usecase.stats

import com.boomingarage.shared.domain.model.UtilizationStats
import com.boomingarage.shared.domain.repository.StatsRepository

class GetDashboardStatsUseCase(private val statsRepository: StatsRepository) {
    suspend fun getDaily(garageId: String, date: String): Result<UtilizationStats> {
        return statsRepository.getDailyStats(garageId, date)
    }

    suspend fun getWeekly(garageId: String, startDate: String): Result<UtilizationStats> {
        return statsRepository.getWeeklyStats(garageId, startDate)
    }

    suspend fun getRevenueHistory(garageId: String, days: Int = 30): Result<List<UtilizationStats>> {
        return statsRepository.getRevenueHistory(garageId, days)
    }
}
