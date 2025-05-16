package com.campusmov.uniride.presentation.profile.model

data class ClassScheduleUiModel(
    val name: String,
    val locationName: String,
    val start: String,
    val end: String,
    val dayOfWeek: String,
    val address: String
)
