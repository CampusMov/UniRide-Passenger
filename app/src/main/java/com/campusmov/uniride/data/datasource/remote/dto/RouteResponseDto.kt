package com.campusmov.uniride.data.datasource.remote.dto

import com.campusmov.uniride.domain.route.model.Route
import com.google.gson.annotations.SerializedName

data class RouteResponseDto(
    @SerializedName("intersections")
    val intersections: List<IntersectionResponseDto>,
    @SerializedName("totalDistance")
    val totalDistance: Double,
    @SerializedName("totalDuration")
    val totalDuration: Double
) {
    fun toDomain(): Route {
        return Route(
            intersections = intersections.map { it.toDomain() },
            totalDistance = totalDistance,
            totalDuration = totalDuration
        )
    }
}