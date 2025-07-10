package com.campusmov.uniride.data.datasource.remote.dto

import android.util.Log
import com.campusmov.uniride.domain.route.model.RouteCarpool
import com.campusmov.uniride.domain.shared.model.Location
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class RouteCarpoolResponsePayload(
    val id: String?,
    val carpoolId: String?,
    val realStartedTime: String?,
    val estimatedEndedTime: String?,
    val estimatedDurationMinutes: Double?,
    val estimatedDistanceKm: Double?,
    val originName: String?,
    val originAddress: String?,
    val originLongitude: Double?,
    val originLatitude: Double?,
    val destinationName: String?,
    val destinationAddress: String?,
    val destinationLongitude: Double?,
    val destinationLatitude: Double?,
    val carpoolCurrentName: String?,
    val carpoolCurrentAddress: String?,
    val carpoolCurrentLongitude: Double?,
    val carpoolCurrentLatitude: Double?
) {
    fun toDomain(): RouteCarpool {
        return RouteCarpool(
            id = id.orEmpty(),
            carpoolId = carpoolId.orEmpty(),
            realStartedTime = realStartedTime?.toDateOrNull(),
            estimatedEndedTime = estimatedEndedTime?.toDateOrNull(),
            estimatedDurationMinutes = estimatedDurationMinutes ?: 0.0,
            estimatedDistanceKm = estimatedDistanceKm ?: 0.0,
            origin = Location(
                name = originName.orEmpty(),
                address = originAddress.orEmpty(),
                longitude = originLongitude ?: 0.0,
                latitude = originLatitude ?: 0.0
            ),
            destination = Location(
                name = destinationName.orEmpty(),
                address = destinationAddress.orEmpty(),
                longitude = destinationLongitude ?: 0.0,
                latitude = destinationLatitude ?: 0.0
            ),
            carpoolCurrentLocation = Location(
                name = carpoolCurrentName.orEmpty(),
                address = carpoolCurrentAddress.orEmpty(),
                longitude = carpoolCurrentLongitude ?: 0.0,
                latitude = carpoolCurrentLatitude ?: 0.0
            )
        )
    }
}

private fun String.toDateOrNull(
    pattern: String = "yyyy-MM-dd'T'HH:mm:ss",
    locale: Locale = Locale.getDefault()
): Date? = try {
    SimpleDateFormat(pattern, locale).parse(this)
} catch (_: Exception) {
    Log.d("TAG", "RouteCarpoolResponsePayload: Invalid date format for string: $this")
    null
}