package com.campusmov.uniride.domain.routingmatching.usecases

import com.campusmov.uniride.domain.routingmatching.repository.PassengerRequestRepository

class GetAllPassengerRequestsByPassengerIdUseCase(private val passengerRequestRepository: PassengerRequestRepository) {
    suspend operator fun invoke(passengerId: String) = passengerRequestRepository.getAllPassengerRequestsByPassengerId(passengerId)
}