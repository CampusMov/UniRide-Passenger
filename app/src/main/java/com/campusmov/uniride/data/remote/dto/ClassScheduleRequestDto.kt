package com.campusmov.uniride.data.remote.dto

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
)