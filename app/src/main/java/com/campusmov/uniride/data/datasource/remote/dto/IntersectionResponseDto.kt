package com.campusmov.uniride.data.datasource.remote.dto

import com.campusmov.uniride.domain.route.model.Intersection
import com.google.gson.annotations.SerializedName

data class IntersectionResponseDto(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
) {
    fun toDomain(): Intersection {
        return Intersection(
            latitude = latitude,
            longitude = longitude
        )
    }
}