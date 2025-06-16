package com.campusmov.uniride.data.datasource.remote.dto

import com.campusmov.uniride.domain.profile.model.ClassSchedule
import retrofit2.http.Field
import java.time.LocalDateTime

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
    val startedAt: LocalDateTime,
    @Field("endedAt")
    val endedAt: LocalDateTime,
    @Field("selectedDay")
    val selectedDay: String
) {
    companion object {
        fun fromDomain(classSchedule: ClassSchedule): ClassScheduleRequestDto {
            return ClassScheduleRequestDto(
                id = classSchedule.id,
                courseName = classSchedule.courseName,
                locationName = classSchedule.locationName,
                latitude = classSchedule.latitude,
                longitude = classSchedule.longitude,
                address = classSchedule.address,
                startedAt = classSchedule.startedAt,
                endedAt = classSchedule.endedAt,
                selectedDay = classSchedule.selectedDay.name
            )
        }
    }
}
