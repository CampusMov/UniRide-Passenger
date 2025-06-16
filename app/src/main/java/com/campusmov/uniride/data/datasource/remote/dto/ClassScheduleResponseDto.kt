package com.campusmov.uniride.data.datasource.remote.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.domain.profile.model.EDay
import retrofit2.http.Field
import java.time.LocalDateTime

data class ClassScheduleResponseDto(
    @Field("id")
    val id: String?,
    @Field("courseName")
    val courseName: String?,
    @Field("locationName")
    val locationName: String?,
    @Field("latitude")
    val latitude: Double?,
    @Field("longitude")
    val longitude: Double?,
    @Field("address")
    val address: String?,
    @Field("startedAt")
    val startedAt: String?,
    @Field("endedAt")
    val endedAt: String?,
    @Field("selectedDay")
    val selectedDay: String?
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDomain(): ClassSchedule {
        return ClassSchedule(
            id = id ?: "",
            courseName = courseName ?: "",
            locationName = locationName ?: "",
            latitude = latitude ?: 0.0,
            longitude = longitude ?: 0.0,
            address = address ?: "",
            startedAt = LocalDateTime.parse(startedAt),
            endedAt = LocalDateTime.parse(endedAt),
            selectedDay = EDay.fromString(selectedDay)
        )
    }
}