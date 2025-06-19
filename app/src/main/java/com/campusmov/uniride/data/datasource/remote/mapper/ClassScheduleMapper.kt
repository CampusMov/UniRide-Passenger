package com.campusmov.uniride.data.datasource.remote.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.campusmov.uniride.data.datasource.remote.dto.ClassScheduleRequestDto
import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.utils.toIsoString

@RequiresApi(Build.VERSION_CODES.O)
fun ClassSchedule.toRequestBody(): ClassScheduleRequestDto =
    ClassScheduleRequestDto(
        id,
        courseName,
        locationName,
        latitude,
        longitude,
        address,
        startedAt.toIsoString(),
        endedAt.toIsoString(),
        selectedDay.name
    )
