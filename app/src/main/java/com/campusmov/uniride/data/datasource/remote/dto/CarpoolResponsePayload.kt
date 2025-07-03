package com.campusmov.uniride.data.datasource.remote.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.campusmov.uniride.domain.routingmatching.model.Carpool
import com.campusmov.uniride.domain.routingmatching.model.ECarpoolStatus
import com.campusmov.uniride.domain.shared.model.EDay
import com.campusmov.uniride.domain.shared.model.Location
import java.time.LocalTime

data class CarpoolResponsePayload(
    val id: String?,
    val driverId: String?,
    val vehicleId: String?,
    val status: String?,
    val availableSeats: Int?,
    val maxPassengers: Int?,
    val scheduleId: String?,
    val radius: Int?,
    val originName: String?,
    val originAddress: String?,
    val originLongitude: Double?,
    val originLatitude: Double?,
    val destinationName: String?,
    val destinationAddress: String?,
    val destinationLongitude: Double?,
    val destinationLatitude: Double?,
    val startedClassTime: String?,
    val endedClassTime: String?,
    val classDay: String?,
    val isVisible: Boolean?
){
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDomain(): Carpool {
        return Carpool(
            id = id.orEmpty(),
            driverId = driverId.orEmpty(),
            vehicleId = vehicleId.orEmpty(),
            status = ECarpoolStatus.fromString(status),
            availableSeats = availableSeats ?: 0,
            maxPassengers = maxPassengers ?: 0,
            scheduleId = scheduleId.orEmpty(),
            radius = radius ?: 0,
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
            startedClassTime = LocalTime.parse(startedClassTime),
            endedClassTime = LocalTime.parse(endedClassTime),
            classDay = EDay.fromString(classDay),
            isVisible = isVisible ?: false
        )
    }
}