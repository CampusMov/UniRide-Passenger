package com.campusmov.uniride.presentation.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun convertLongMillisToLocalDate(
    millis: Long,
    zone: ZoneId = ZoneId.systemDefault()
): LocalDate {
    return Instant
        .ofEpochMilli(millis)
        .atZone(zone)
        .toLocalDate()
}

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toLocalDate(
    zone: ZoneId = ZoneId.systemDefault()
): LocalDate = convertLongMillisToLocalDate(this, zone)