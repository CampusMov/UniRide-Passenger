package com.campusmov.uniride.data.repository.routingmatching

import com.campusmov.uniride.core.Config.ROUTING_MATCHING_SERVICE_NAME
import com.campusmov.uniride.core.Config.SERVICES_WEBSOCKET_URL
import com.campusmov.uniride.data.datasource.remote.dto.PassengerRequestResponsePayload
import com.campusmov.uniride.domain.routingmatching.model.PassengerRequest
import com.campusmov.uniride.domain.routingmatching.repository.PassengerRequestWebSocketRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.subscribeText

class PassengerRequestWebSocketRepositoryImpl(
    private val stompClient: StompClient,
    private val gson: Gson
): PassengerRequestWebSocketRepository {
    private var stompSession: StompSession? = null

    override suspend fun connectRequestsSession() {
        stompSession = stompClient.connect("$SERVICES_WEBSOCKET_URL$ROUTING_MATCHING_SERVICE_NAME/ws")
    }

    override suspend fun subscribeToRequestStatus(passengerRequestId: String): Flow<PassengerRequest> {
        return stompSession!!
            .subscribeText("/topic/passenger-request/$passengerRequestId/status")
            .map { json ->
                gson.fromJson(json, PassengerRequestResponsePayload::class.java).toDomain()
            }
    }

    override suspend fun disconnectRequestsSession() {
        stompSession?.disconnect()
    }
}