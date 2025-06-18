package com.campusmov.uniride.data.datasource.remote.mapper

import com.campusmov.uniride.data.datasource.remote.dto.ClassScheduleRequestDto
import com.campusmov.uniride.domain.profile.model.ClassSchedule

fun ClassSchedule.toRequestBody(): ClassScheduleRequestDto =
    ClassScheduleRequestDto(
        id,
        courseName,
        locationName,
        latitude,
        longitude,
        address,
        startedAt,
        endedAt,
        selectedDay.name
    )
