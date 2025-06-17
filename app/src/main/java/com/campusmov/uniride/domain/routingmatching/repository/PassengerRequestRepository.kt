package com.campusmov.uniride.domain.routingmatching.repository

import com.campusmov.uniride.domain.routingmatching.model.PassengerRequest
import com.campusmov.uniride.domain.shared.util.Resource

interface PassengerRequestRepository {
    suspend fun savePassengerRequest(passengerRequest: PassengerRequest): Resource<Unit>
}