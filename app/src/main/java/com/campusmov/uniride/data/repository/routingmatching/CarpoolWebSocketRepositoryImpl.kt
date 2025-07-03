package com.campusmov.uniride.data.repository.routingmatching

import android.os.Build
import androidx.annotation.RequiresApi
import com.campusmov.uniride.core.Config.ROUTING_MATCHING_SERVICE_NAME
import com.campusmov.uniride.core.Config.SERVICES_WEBSOCKET_URL
import com.campusmov.uniride.data.datasource.remote.dto.CarpoolResponsePayload
import com.campusmov.uniride.domain.routingmatching.model.Carpool
import com.campusmov.uniride.domain.routingmatching.repository.CarpoolWebSocketRepository
import com.google.gson.Gson
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.subscribeText

class CarpoolWebSocketRepositoryImpl(
    private val stompClient: StompClient,
    private val gson: Gson
): CarpoolWebSocketRepository {
    private var stompSession: StompSession? = null
    private val sessionReady = CompletableDeferred<Unit>()

    override suspend fun connectCarpoolSession() {
        stompSession = stompClient.connect("$SERVICES_WEBSOCKET_URL$ROUTING_MATCHING_SERVICE_NAME/ws")
        if (!sessionReady.isCompleted) sessionReady.complete(Unit)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun subscribeToCarpoolStatus(carpoolId: String): Flow<Carpool> {
        awaitConnectionReady()
        val session = stompSession
            ?: throw IllegalStateException("First, connect to the WebSocket session using connectCarpoolSession()")
        return session
            .subscribeText("/topic/carpool/$carpoolId/status")
            .map { json ->
                gson.fromJson(json, CarpoolResponsePayload::class.java).toDomain()
            }
    }

    override suspend fun disconnectCarpoolSession() {
        stompSession?.disconnect()
    }

    override suspend fun awaitConnectionReady() {
        sessionReady.await()
    }
}