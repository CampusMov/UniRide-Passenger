package com.campusmov.uniride.domain.route.repository

import com.campusmov.uniride.domain.route.model.Route
import com.campusmov.uniride.domain.shared.util.Resource

interface RouteRepository {
    suspend fun getRoute(startLatitude: Double, startLongitude: Double, endLatitude: Double, endLongitude: Double): Resource<Route>
}