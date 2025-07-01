package com.campusmov.uniride.domain.routingmatching.repository

import com.campusmov.uniride.domain.routingmatching.model.PassengerRequest
import kotlinx.coroutines.flow.Flow

interface PassengerRequestWebSocketRepository {
    suspend fun connectRequestsSession()
    suspend fun subscribeToRequestStatus(passengerRequestId: String): Flow<PassengerRequest>
    suspend fun disconnectRequestsSession()
    suspend fun awaitConnectionReady()
}