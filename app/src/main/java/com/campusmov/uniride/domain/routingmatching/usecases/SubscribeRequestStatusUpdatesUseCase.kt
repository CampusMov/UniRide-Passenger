package com.campusmov.uniride.domain.routingmatching.usecases

import com.campusmov.uniride.domain.routingmatching.model.PassengerRequest
import com.campusmov.uniride.domain.routingmatching.repository.PassengerRequestWebSocketRepository
import kotlinx.coroutines.flow.Flow

class SubscribeRequestStatusUpdatesUseCase(private val passengerRequestWebSocketRepository: PassengerRequestWebSocketRepository) {
    suspend operator fun invoke(passengerRequestId: String): Flow<PassengerRequest> = passengerRequestWebSocketRepository.subscribeToRequestStatus(passengerRequestId)
}