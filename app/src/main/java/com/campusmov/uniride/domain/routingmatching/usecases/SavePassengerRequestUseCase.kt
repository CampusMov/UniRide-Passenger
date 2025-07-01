package com.campusmov.uniride.domain.routingmatching.usecases

import com.campusmov.uniride.domain.routingmatching.model.PassengerRequest
import com.campusmov.uniride.domain.routingmatching.repository.PassengerRequestRepository

class SavePassengerRequestUseCase(private val passengerRequestRepository: PassengerRequestRepository) {
    suspend operator fun invoke(passengerRequest: PassengerRequest) = passengerRequestRepository.savePassengerRequest(passengerRequest)
}
