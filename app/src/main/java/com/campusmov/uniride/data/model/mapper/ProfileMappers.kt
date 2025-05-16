package com.campusmov.uniride.data.model.mapper

import com.campusmov.uniride.data.remote.dto.ClassScheduleRequestDto
import com.campusmov.uniride.data.remote.dto.ProfileRequestDto
import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.domain.profile.model.Profile

fun Profile.toRequestBody(): ProfileRequestDto =
    ProfileRequestDto(
        userId,
        institutionalEmailAddress,
        personalEmailAddress,
        countryCode,
        phoneNumber,
        firstName,
        lastName,
        birthDate,
        gender,
        profilePictureUrl,
        university,
        faculty,
        academicProgram,
        semester,
        classSchedules.map { it.toRequestBody() }
    )

fun ClassSchedule.toRequestBody(): ClassScheduleRequestDto =
    ClassScheduleRequestDto(
        courseName,
        locationName,
        latitude,
        longitude,
        address,
        startedAt,
        endedAt,
        selectedDay
    )
