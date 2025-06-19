package com.campusmov.uniride.data.datasource.remote.mapper

import com.campusmov.uniride.data.datasource.remote.dto.LocationRequestDto
import com.campusmov.uniride.domain.shared.model.Location

fun Location.toRequestBody(): LocationRequestDto =
    LocationRequestDto(
        name = name,
        address = address,
        latitude = latitude,
        longitude = longitude
    )