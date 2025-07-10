package com.campusmov.uniride.data.repository.route

import com.campusmov.uniride.core.Config
import com.campusmov.uniride.data.datasource.remote.dto.RouteCarpoolResponsePayload
import com.campusmov.uniride.domain.route.model.RouteCarpool
import com.campusmov.uniride.domain.route.repository.RouteCarpoolWebSocketRepository
import com.google.gson.Gson
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.subscribeText

class RouteCarpoolWebSocketRepositoryRepositoryImpl(
    private val stompClient: StompClient,
    private val gson: Gson
): RouteCarpoolWebSocketRepository {
    private var stompSession: StompSession? = null
    private val sessionReady = CompletableDeferred<Unit>()

    override suspend fun connectRouteCarpoolSession() {
        stompSession = stompClient.connect("${Config.SERVICES_WEBSOCKET_URL}${Config.ROUTING_MATCHING_SERVICE_NAME}/ws")
        if (!sessionReady.isCompleted) sessionReady.complete(Unit)
    }

    override suspend fun subscribeToRouteCarpoolUpdateCurrentLocation(carpoolId: String): Flow<RouteCarpool> {
        awaitConnectionReady()
        val session = stompSession
            ?: throw IllegalStateException("First, connect to the WebSocket session using connectRequestsSession()")
        return session
            .subscribeText("/topic/carpool/$carpoolId/route")
            .map { json ->
                gson.fromJson(json, RouteCarpoolResponsePayload::class.java).toDomain()
            }
    }

    override suspend fun disconnectRouteCarpoolSession() {
        stompSession?.disconnect()
    }

    override suspend fun awaitConnectionReady() {
        sessionReady.await()
    }
}