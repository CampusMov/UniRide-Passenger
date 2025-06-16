package com.campusmov.uniride.data.datasource.remote.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.campusmov.uniride.domain.routingmatching.model.Carpool
import com.campusmov.uniride.domain.routingmatching.model.ECarpoolStatus
import com.campusmov.uniride.domain.shared.model.EDay
import com.campusmov.uniride.domain.shared.model.Location
import retrofit2.http.Field
import java.time.LocalDateTime

data class CarpoolResponseDto (
    @Field("id")
    val id: String?,
    @Field("driverId")
    val driverId: String?,
    @Field("vehicleId")
    val vehicleId: String?,
    @Field("status")
    val status: String?,
    @Field("availableSeats")
    val availableSeats: Int?,
    @Field("maxPassengers")
    val maxPassengers: Int?,
    @Field("scheduleId")
    val scheduleId: String?,
    @Field("radius")
    val radius: Int?,
    @Field("originName")
    val originName: String?,
    @Field("originAddress")
    val originAddress: String?,
    @Field("originLongitude")
    val originLongitude: Double?,
    @Field("originLatitude")
    val originLatitude: Double?,
    @Field("destinationName")
    val destinationName: String?,
    @Field("destinationAddress")
    val destinationAddress: String?,
    @Field("destinationLongitude")
    val destinationLongitude: Double?,
    @Field("destinationLatitude")
    val destinationLatitude: Double?,
    @Field("startedClassTime")
    val startedClassTime: String?,
    @Field("endedClassTime")
    val endedClassTime: String?,
    @Field("classDay")
    val classDay: String?,
    @Field("isVisible")
    val isVisible: Boolean?
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDomain(): Carpool{
        return Carpool(
            id = id ?: "",
            driverId = driverId ?: "",
            vehicleId = vehicleId ?: "",
            status = ECarpoolStatus.fromString(status),
            availableSeats = availableSeats ?: 0,
            maxPassengers = maxPassengers ?: 0,
            scheduleId = scheduleId ?: "",
            radius = radius ?: 0,
            origin = Location(
                name = originName ?: "",
                address = originAddress ?: "",
                longitude = originLongitude ?: 0.0,
                latitude = originLatitude ?: 0.0
            ),
            destination = Location(
                name = destinationName ?: "",
                address = destinationAddress ?: "",
                longitude = destinationLongitude ?: 0.0,
                latitude = destinationLatitude ?: 0.0
            ),
            startedClassTime = LocalDateTime.parse(startedClassTime),
            endedClassTime = LocalDateTime.parse(endedClassTime),
            classDay = EDay.fromString(classDay),
            isVisible = isVisible == true
        )
    }
}