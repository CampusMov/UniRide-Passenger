package com.campusmov.uniride.domain.routingmatching.usecases

import com.campusmov.uniride.domain.routingmatching.repository.CarpoolWebSocketRepository

class SubscribeCarpoolStatusUpdatesUseCase(private val carpoolWebSocketRepository: CarpoolWebSocketRepository) {
    suspend operator fun invoke(carpoolId: String) = carpoolWebSocketRepository.subscribeToCarpoolStatus(carpoolId)
}