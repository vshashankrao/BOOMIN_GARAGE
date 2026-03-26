package com.boomingarage.shared.domain.repository

import com.boomingarage.shared.domain.model.UtilizationStats

interface StatsRepository {
    suspend fun getDailyStats(garageId: String, date: String): Result<UtilizationStats>
    suspend fun getWeeklyStats(garageId: String, startDate: String): Result<UtilizationStats>
    suspend fun getRevenueHistory(garageId: String, days: Int = 30): Result<List<UtilizationStats>>
}
