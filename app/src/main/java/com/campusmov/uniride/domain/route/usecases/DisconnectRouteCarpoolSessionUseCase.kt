package com.campusmov.uniride.domain.route.usecases

import com.campusmov.uniride.domain.route.repository.RouteCarpoolWebSocketRepository

class DisconnectRouteCarpoolSessionUseCase(private val routeCarpoolWebSocketRepository: RouteCarpoolWebSocketRepository) {
    suspend operator fun invoke() = routeCarpoolWebSocketRepository.disconnectRouteCarpoolSession()
}