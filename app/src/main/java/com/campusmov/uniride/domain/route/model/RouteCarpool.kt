package com.campusmov.uniride.domain.route.model

import com.campusmov.uniride.domain.shared.model.Location
import java.util.Date

data class RouteCarpool(
    val id: String = "1",
    val carpoolId: String,
    val realStartedTime: Date? = null,
    val estimatedEndedTime: Date? = null,
    val estimatedDurationMinutes: Double? = null,
    val estimatedDistanceKm: Double? = null,
    val origin: Location,
    val destination: Location,
    val carpoolCurrentLocation: Location,
)