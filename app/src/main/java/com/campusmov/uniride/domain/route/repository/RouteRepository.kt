package com.campusmov.uniride.domain.route.repository

import com.campusmov.uniride.domain.route.model.Route

interface RouteRepository {
    suspend fun getRoute(): Route

    suspend fun getRoute(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double,
        algorithm: String
    ): Route
}