package com.campusmov.uniride.domain.intripcommunication.repository

import com.campusmov.uniride.domain.intripcommunication.model.Message
import kotlinx.coroutines.flow.Flow

interface InTripCommunicationWebSocketRepository {
    suspend fun connectSession()
    suspend fun subscribeToChat(chatId: String): Flow<Message>
    suspend fun sendMessage(chatId: String, senderId: String, content: String)
    suspend fun disconnectSession()
    suspend fun awaitConnectionReady()
}
