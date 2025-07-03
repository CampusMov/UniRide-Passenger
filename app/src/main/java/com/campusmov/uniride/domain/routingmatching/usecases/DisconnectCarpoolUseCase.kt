package com.campusmov.uniride.domain.routingmatching.usecases

import com.campusmov.uniride.domain.routingmatching.repository.CarpoolWebSocketRepository

class DisconnectCarpoolUseCase(private val carpoolWebSocketRepository: CarpoolWebSocketRepository) {
    suspend operator fun invoke() = carpoolWebSocketRepository.disconnectCarpoolSession()
}