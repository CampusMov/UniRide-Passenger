package com.campusmov.uniride.data.datasource.remote.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.domain.shared.model.Location
import com.campusmov.uniride.utils.toIsoString
import retrofit2.http.Field

data class SearchAvailableCarpoolsRequestDto(
    @Field("startedClassTime")
    val startedClassTime: String,
    @Field("originName")
    val originName: String,
    @Field("originAddress")
    val originAddress: String,
    @Field("originLongitude")
    val originLongitude: Double,
    @Field("originLatitude")
    val originLatitude: Double,
    @Field("destinationName")
    val destinationName: String,
    @Field("destinationAddress")
    val destinationAddress: String,
    @Field("destinationLongitude")
    val destinationLongitude: Double,
    @Field("destinationLatitude")
    val destinationLatitude: Double,
    @Field("requestedSeats")
    val requestedSeats: Int,
    @Field("classDay")
    val classDay: String
)
{
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun fromDomain(location: Location, classSchedule: ClassSchedule, requestedSeats: Int): SearchAvailableCarpoolsRequestDto {
            return SearchAvailableCarpoolsRequestDto(
                startedClassTime = classSchedule.startedAt.toIsoString(),
                originName = location.name,
                originAddress = location.address,
                originLongitude = location.longitude,
                originLatitude = location.latitude,
                destinationName = classSchedule.locationName,
                destinationAddress = classSchedule.address,
                destinationLongitude = classSchedule.longitude,
                destinationLatitude = classSchedule.latitude,
                requestedSeats = requestedSeats,
                classDay = classSchedule.selectedDay.name
            )
        }
    }
}