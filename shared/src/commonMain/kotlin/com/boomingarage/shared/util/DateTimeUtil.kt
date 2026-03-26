package com.boomingarage.shared.util

import kotlinx.datetime.*

object DateTimeUtil {
    fun now(): Instant = Clock.System.now()

    fun today(): LocalDate = now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    fun currentTime(): LocalTime = now().toLocalDateTime(TimeZone.currentSystemDefault()).time

    fun toEpochMillis(instant: Instant): Long = instant.toEpochMilliseconds()

    fun fromEpochMillis(millis: Long): Instant = Instant.fromEpochMilliseconds(millis)

    fun formatDate(date: String): String {
        // "2026-03-25" -> "Mar 25, 2026"
        val parsed = LocalDate.parse(date)
        val month = parsed.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
        return "$month ${parsed.dayOfMonth}, ${parsed.year}"
    }

    fun formatTime(time: String): String {
        // "14:00" -> "2:00 PM"
        val parts = time.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1]
        val amPm = if (hour >= 12) "PM" else "AM"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        return "$displayHour:$minute $amPm"
    }

    fun generateTimeSlots(openTime: String, closeTime: String): List<String> {
        val open = openTime.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
        val close = closeTime.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
        val slots = mutableListOf<String>()
        var current = open
        while (current < close) {
            val h = current / 60
            val m = current % 60
            slots.add("${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}")
            current += Constants.TIME_SLOT_DURATION_MINUTES
        }
        return slots
    }

    fun calculateDurationMinutes(startTime: String, endTime: String): Int {
        val start = startTime.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
        val end = endTime.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
        return end - start
    }
}
