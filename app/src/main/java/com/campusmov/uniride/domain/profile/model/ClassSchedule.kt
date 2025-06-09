package com.campusmov.uniride.domain.profile.model

data class ClassSchedule(
    val courseName: String,
    val locationName: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String,
    val startedAt: String,
    val endedAt: String,
    val selectedDay: String
)