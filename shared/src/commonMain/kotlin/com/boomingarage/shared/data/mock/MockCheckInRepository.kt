package com.boomingarage.shared.data.mock

import com.boomingarage.shared.domain.model.CheckInSession
import com.boomingarage.shared.domain.repository.CheckInRepository
import com.boomingarage.shared.util.DateTimeUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MockCheckInRepository : CheckInRepository {
    private val sessions = MutableStateFlow<List<CheckInSession>>(emptyList())
    private var nextId = 1

    override suspend fun startSession(bookingId: String): Result<CheckInSession> {
        val now = DateTimeUtil.toEpochMillis(DateTimeUtil.now())
        val session = CheckInSession(
            id = "session-${nextId++}",
            bookingId = bookingId,
            userId = "cust-001",
            garageId = "garage-001",
            bayId = "bay-001",
            startedAt = now,
            scheduledEndAt = now + (2 * 3600 * 1000L) // 2 hours
        )
        sessions.value = sessions.value + session
        return Result.success(session)
    }

    override suspend fun endSession(sessionId: String): Result<CheckInSession> {
        val now = DateTimeUtil.toEpochMillis(DateTimeUtil.now())
        var ended: CheckInSession? = null
        sessions.value = sessions.value.map {
            if (it.id == sessionId) {
                ended = it.copy(actualEndAt = now)
                ended!!
            } else it
        }
        return ended?.let { Result.success(it) }
            ?: Result.failure(Exception("Session not found"))
    }

    override suspend fun extendSession(sessionId: String, additionalMinutes: Int): Result<CheckInSession> {
        var updated: CheckInSession? = null
        sessions.value = sessions.value.map {
            if (it.id == sessionId) {
                updated = it.copy(scheduledEndAt = it.scheduledEndAt + (additionalMinutes * 60 * 1000L))
                updated!!
            } else it
        }
        return updated?.let { Result.success(it) }
            ?: Result.failure(Exception("Session not found"))
    }

    override fun observeActiveSession(userId: String): Flow<CheckInSession?> {
        return sessions.map { list ->
            list.find { it.userId == userId && it.actualEndAt == null }
        }
    }

    override suspend fun getActiveSession(userId: String): Result<CheckInSession?> {
        return Result.success(
            sessions.value.find { it.userId == userId && it.actualEndAt == null }
        )
    }

    override suspend fun requestAssistance(sessionId: String, message: String): Result<Unit> {
        return Result.success(Unit)
    }
}
