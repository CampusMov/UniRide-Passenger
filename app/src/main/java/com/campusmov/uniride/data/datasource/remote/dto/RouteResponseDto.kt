package com.campusmov.uniride.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class RouteResponseDto(
    @SerializedName("intersections")
    val intersections: List<IntersectionDto>,
    @SerializedName("totalDistance")
    val totalDistance: Double,
    @SerializedName("totalDuration")
    val totalDuration: Double
)