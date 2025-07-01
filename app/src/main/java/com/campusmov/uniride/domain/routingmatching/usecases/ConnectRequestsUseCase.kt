package com.campusmov.uniride.domain.routingmatching.usecases

import com.campusmov.uniride.domain.routingmatching.repository.PassengerRequestWebSocketRepository

class ConnectRequestsUseCase(private val passengerRequestWebSocketRepository: PassengerRequestWebSocketRepository) {
    suspend operator fun invoke() = passengerRequestWebSocketRepository.connectRequestsSession()
}