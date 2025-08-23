package com.juanpablo0612.sigat.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime


@OptIn(FormatStringsInDatetimeFormats::class, ExperimentalTime::class)
fun timestampToDayMonthYearFormat(timestamp: Long): String {
    val instant = Instant.fromEpochMilliseconds(timestamp)
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val formatPattern = "dd/MM/yyyy"
    val dateTimeFormat = LocalDate.Format {
        byUnicodePattern(formatPattern)
    }

    return localDate.format(dateTimeFormat)
}