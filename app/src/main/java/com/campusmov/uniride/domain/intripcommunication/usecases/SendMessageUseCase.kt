package com.campusmov.uniride.domain.intripcommunication.usecases

import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationWebSocketRepository
import com.campusmov.uniride.domain.shared.util.Resource

class SendMessageUseCase(
    private val repository: InTripCommunicationWebSocketRepository
) {
    suspend operator fun invoke(
        chatId: String,
        senderId: String,
        content: String
    ): Resource<Unit> {
        if (chatId.isBlank() || senderId.isBlank()) {
            return Resource.Failure("The chat ID and sender ID cannot be empty")
        }
        if (content.isBlank()) {
            return Resource.Failure("The message content cannot be empty")
        }
        return try {
            repository.sendMessage(chatId, senderId, content)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Failure(e.message ?: "An error occurred while sending the message")
        }
    }
}