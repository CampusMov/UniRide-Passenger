package com.campusmov.uniride.domain.routingmatching.usecases

import com.campusmov.uniride.domain.routingmatching.repository.CarpoolWebSocketRepository

class ConnectCarpoolUseCase(private val carpoolWebSocketRepository: CarpoolWebSocketRepository) {
    suspend operator fun invoke() = carpoolWebSocketRepository.connectCarpoolSession()
}