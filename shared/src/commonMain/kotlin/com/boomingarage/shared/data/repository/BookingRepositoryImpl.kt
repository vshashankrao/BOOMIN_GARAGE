package com.boomingarage.shared.data.repository

import com.boomingarage.shared.data.mapper.BookingMapper
import com.boomingarage.shared.data.remote.dto.BookingDto
import com.boomingarage.shared.domain.model.Booking
import com.boomingarage.shared.domain.model.BookingStatus
import com.boomingarage.shared.domain.repository.BookingRepository
import com.boomingarage.shared.util.Constants
import com.boomingarage.shared.util.DateTimeUtil
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookingRepositoryImpl : BookingRepository {
    private val firestore = Firebase.firestore
    private val collection = firestore.collection(Constants.COLLECTION_BOOKINGS)

    override suspend fun create(booking: Booking): Result<Booking> = runCatching {
        val dto = BookingMapper.toDto(booking).copy(
            createdAt = DateTimeUtil.toEpochMillis(DateTimeUtil.now())
        )
        val docRef = collection.add(dto)
        val created = dto.copy(id = docRef.id)
        BookingMapper.toDomain(created)
    }

    override suspend fun cancel(bookingId: String): Result<Unit> = runCatching {
        collection.document(bookingId).update(mapOf("status" to BookingStatus.CANCELLED.name))
    }

    override suspend fun updateStatus(bookingId: String, status: BookingStatus): Result<Unit> = runCatching {
        collection.document(bookingId).update(mapOf("status" to status.name))
    }

    override suspend fun getById(bookingId: String): Result<Booking> = runCatching {
        val doc = collection.document(bookingId).get()
        val dto = doc.data<BookingDto>()
        BookingMapper.toDomain(dto.copy(id = doc.id))
    }

    override fun getByUser(userId: String): Flow<List<Booking>> {
        return collection.where { "userId" equalTo userId }
            .snapshots
            .map { snapshot ->
                snapshot.documents.map { doc ->
                    BookingMapper.toDomain(doc.data<BookingDto>().copy(id = doc.id))
                }.sortedByDescending { it.createdAt }
            }
    }

    override fun getByGarage(garageId: String, date: String?): Flow<List<Booking>> {
        val query = collection.where { "garageId" equalTo garageId }
        return query.snapshots.map { snapshot ->
            snapshot.documents.map { doc ->
                BookingMapper.toDomain(doc.data<BookingDto>().copy(id = doc.id))
            }.let { bookings ->
                if (date != null) bookings.filter { it.date == date } else bookings
            }.sortedBy { it.date + it.startTime }
        }
    }

    override suspend fun getUpcomingByUser(userId: String): Result<List<Booking>> = runCatching {
        val today = DateTimeUtil.today().toString()
        val snapshot = collection.where { "userId" equalTo userId }.get()
        snapshot.documents.map { doc ->
            BookingMapper.toDomain(doc.data<BookingDto>().copy(id = doc.id))
        }.filter {
            it.date >= today && it.status in listOf(BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.CHECKED_IN)
        }.sortedBy { it.date + it.startTime }
    }

    override suspend fun getPastByUser(userId: String): Result<List<Booking>> = runCatching {
        val today = DateTimeUtil.today().toString()
        val snapshot = collection.where { "userId" equalTo userId }.get()
        snapshot.documents.map { doc ->
            BookingMapper.toDomain(doc.data<BookingDto>().copy(id = doc.id))
        }.filter {
            it.date < today || it.status in listOf(BookingStatus.COMPLETED, BookingStatus.CANCELLED)
        }.sortedByDescending { it.date + it.startTime }
    }
}
