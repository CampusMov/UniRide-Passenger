package com.campusmov.uniride.data.datasource.remote.dto

import com.campusmov.uniride.domain.profile.model.ClassSchedule
import retrofit2.http.Field

data class ClassScheduleRequestDto(
    @Field("courseName") val courseName: String,
    @Field("locationName") val locationName: String,
    @Field("latitude") val latitude: Double,
    @Field("longitude") val longitude: Double,
    @Field("address") val address: String,
    @Field("startedAt") val startedAt: String,
    @Field("endedAt") val endedAt: String,
    @Field("selectedDay") val selectedDay: String
) {
    companion object {
        fun fromDomain(classSchedule: ClassSchedule): ClassScheduleRequestDto {
            return ClassScheduleRequestDto(
                courseName = classSchedule.courseName,
                locationName = classSchedule.locationName,
                latitude = classSchedule.latitude,
                longitude = classSchedule.longitude,
                address = classSchedule.address,
                startedAt = classSchedule.startedAt,
                endedAt = classSchedule.endedAt,
                selectedDay = classSchedule.selectedDay
            )
        }
    }
}
