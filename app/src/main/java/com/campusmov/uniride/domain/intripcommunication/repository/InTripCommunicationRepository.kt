package com.campusmov.uniride.domain.intripcommunication.repository

import com.campusmov.uniride.domain.intripcommunication.model.Chat
import com.campusmov.uniride.domain.intripcommunication.model.Message
import com.campusmov.uniride.domain.shared.util.Resource

interface InTripCommunicationRepository {
    suspend fun createChat(
        carpoolId: String,
        driverId: String,
        passengerId: String
    ): Resource<Chat>

    suspend fun closeChat(
        chatId: String
    ): Resource<Unit>

    suspend fun getPassengerChat(
        passengerId: String,
        carpoolId: String
    ): Resource<Chat>

    suspend fun getMessages(
        chatId: String
    ): Resource<List<Message>>

    suspend fun sendMessage(
        chatId: String,
        senderId: String,
        content: String
    ): Resource<Message>

    suspend fun markMessageAsRead(
        chatId: String,
        messageId: String,
        readerId: String
    ): Resource<Unit>
}
