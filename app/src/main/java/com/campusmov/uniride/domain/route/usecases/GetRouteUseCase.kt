package com.campusmov.uniride.domain.route.usecases

import com.campusmov.uniride.domain.route.repository.RouteRepository

class GetRouteUseCase(private val routeRepository: RouteRepository) {
    suspend operator fun invoke(startLatitude: Double,startLongitude: Double, endLatitude: Double, endLongitude: Double) =
        routeRepository.getRoute(startLatitude, startLongitude, endLatitude, endLongitude)
}