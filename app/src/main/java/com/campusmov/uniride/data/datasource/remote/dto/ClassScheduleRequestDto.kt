package com.campusmov.uniride.data.datasource.remote.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.utils.toIsoString
import retrofit2.http.Field

data class ClassScheduleRequestDto(
    @Field("id")
    val id: String?,
    @Field("courseName")
    val courseName: String?,
    @Field("locationName")
    val locationName: String?,
    @Field("latitude")
    val latitude: Double,
    @Field("longitude")
    val longitude: Double,
    @Field("address")
    val address: String,
    @Field("startedAt")
    val startedAt: String,
    @Field("endedAt")
    val endedAt: String,
    @Field("selectedDay")
    val selectedDay: String
) {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun fromDomain(classSchedule: ClassSchedule): ClassScheduleRequestDto {
            return ClassScheduleRequestDto(
                id = classSchedule.id,
                courseName = classSchedule.courseName,
                locationName = classSchedule.locationName,
                latitude = classSchedule.latitude,
                longitude = classSchedule.longitude,
                address = classSchedule.address,
                startedAt = classSchedule.startedAt.toIsoString(),
                endedAt = classSchedule.endedAt.toIsoString(),
                selectedDay = classSchedule.selectedDay.name
            )
        }
    }
}
