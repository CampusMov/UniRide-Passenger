package com.campusmov.uniride.domain.route.usecases

import com.campusmov.uniride.domain.route.model.Route
import com.campusmov.uniride.domain.route.repository.RouteRepository

class GetRouteUseCase(private val routeRepository: RouteRepository) {

    suspend operator fun invoke(): Route =
        routeRepository.getRoute()

    suspend operator fun invoke(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double,
        algorithm: String
    ): Route =
        routeRepository.getRoute(
            startLatitude = startLatitude,
            startLongitude = startLongitude,
            endLatitude = endLatitude,
            endLongitude = endLongitude,
            algorithm = algorithm
        )
}