package com.boomingarage.shared.domain.repository

import com.boomingarage.shared.domain.model.CheckInSession
import kotlinx.coroutines.flow.Flow

interface CheckInRepository {
    suspend fun startSession(bookingId: String): Result<CheckInSession>
    suspend fun endSession(sessionId: String): Result<CheckInSession>
    suspend fun extendSession(sessionId: String, additionalMinutes: Int): Result<CheckInSession>
    fun observeActiveSession(userId: String): Flow<CheckInSession?>
    suspend fun getActiveSession(userId: String): Result<CheckInSession?>
    suspend fun requestAssistance(sessionId: String, message: String): Result<Unit>
}
