package com.campusmov.uniride.domain.intripcommunication.usecases

import com.campusmov.uniride.domain.intripcommunication.model.Message
import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationRepository
import com.campusmov.uniride.domain.shared.util.Resource

class SendMessageUseCase(
    private val repository: InTripCommunicationRepository
) {
    suspend operator fun invoke(
        chatId: String,
        senderId: String,
        content: String
    ): Resource<Message> {
        if (chatId.isBlank() || senderId.isBlank()) {
            return Resource.Failure("Chat ID and sender ID must be provided")
        }
        if (content.isBlank()) {
            return Resource.Failure("Message content cannot be empty")
        }
        return when (val repoResult = repository.sendMessage(chatId, senderId, content)) {
            is Resource.Success -> Resource.Success(repoResult.data)
            is Resource.Failure -> Resource.Failure(repoResult.message)
            is Resource.Loading -> Resource.Loading
        }
    }
}
