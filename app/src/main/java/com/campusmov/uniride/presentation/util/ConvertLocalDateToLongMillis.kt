package com.campusmov.uniride.presentation.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun convertLocalDateToMillis(
    date: LocalDate,
    zone: ZoneId = ZoneId.systemDefault()
): Long {
    return date
        .atStartOfDay(zone)
        .toInstant()
        .toEpochMilli()
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toEpochMillis(
    zone: ZoneId = ZoneId.systemDefault()
): Long = convertLocalDateToMillis(this, zone)