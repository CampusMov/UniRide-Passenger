package com.campusmov.uniride.data.repository.intripcommunication

import com.campusmov.uniride.core.Config.SERVICES_WEBSOCKET_URL
import com.campusmov.uniride.core.Config.IN_TRIP_COMMUNICATION_SERVICE_NAME
import com.campusmov.uniride.domain.intripcommunication.dto.MessageDto
import com.campusmov.uniride.domain.intripcommunication.dto.toDomain
import com.campusmov.uniride.domain.intripcommunication.model.Message
import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationWebSocketRepository
import com.campusmov.uniride.domain.intripcommunication.request.SendMessageRequest
import com.google.gson.Gson
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribeText
import kotlin.jvm.java

class InTripCommunicationWebSocketRepositoryImpl(
    private val stompClient: StompClient,
    private val gson: Gson
) : InTripCommunicationWebSocketRepository {

    private var stompSession: StompSession? = null
    private val sessionReady = CompletableDeferred<Unit>()

    override suspend fun connectSession() {
        val endpointUrl = "$SERVICES_WEBSOCKET_URL$IN_TRIP_COMMUNICATION_SERVICE_NAME/ws"
        stompSession = stompClient.connect(endpointUrl)

        if (!sessionReady.isCompleted) {
            sessionReady.complete(Unit)
        }
    }

    override suspend fun subscribeToChat(chatId: String): Flow<Message> {
        awaitConnectionReady()
        val session = stompSession
            ?: throw IllegalStateException("The WebSocket session is not connected. Call connectSession() first.")

        val topicPath = "/topic/chats/$chatId"

        return session
            .subscribeText(topicPath)
            .map { json ->
                gson.fromJson(json, MessageDto::class.java).toDomain()
            }
    }

    override suspend fun sendMessage(chatId: String, senderId: String, content: String) {
        awaitConnectionReady()
        val session = stompSession
            ?: throw IllegalStateException("The WebSocket session is not connected. Call connectSession() first.")

        val messageRequest = SendMessageRequest(
            senderId = senderId,
            content = content
        )

        val destination = "/app/chat/$chatId/send"

        session.sendText(destination, gson.toJson(messageRequest))
    }

    override suspend fun disconnectSession() {
        stompSession?.disconnect()
        stompSession = null
    }

    override suspend fun awaitConnectionReady() {
        sessionReady.await()
    }
}