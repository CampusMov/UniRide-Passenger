package com.campusmov.uniride.domain.route.model

data class Route(
    val intersections: List<Intersection>,
    val totalDistance: Double,
    val totalDuration: Double
)