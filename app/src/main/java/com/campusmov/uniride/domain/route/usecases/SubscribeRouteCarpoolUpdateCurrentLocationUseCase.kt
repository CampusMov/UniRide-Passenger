package com.campusmov.uniride.domain.route.usecases

import com.campusmov.uniride.domain.route.repository.RouteCarpoolWebSocketRepository

class SubscribeRouteCarpoolUpdateCurrentLocationUseCase(private val routeCarpoolWebSocketRepository: RouteCarpoolWebSocketRepository) {
    suspend operator fun invoke(routeCarpoolId: String) = routeCarpoolWebSocketRepository.subscribeToRouteCarpoolUpdateCurrentLocation(routeCarpoolId)
}