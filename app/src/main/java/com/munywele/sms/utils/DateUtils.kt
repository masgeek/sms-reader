package com.munywele.sms.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtils {
    fun formatDateFromTimestamp(timestamp: Long): String {
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss")
            .withZone(ZoneId.systemDefault()) // Converts to your system's default time zone

        val instant = Instant.ofEpochMilli(timestamp)
        return formatter.format(instant)
    }
}