package com.campusmov.uniride.domain.intripcommunication.usecases

import com.campusmov.uniride.domain.intripcommunication.model.Message
import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationWebSocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribeToChatUseCase @Inject constructor(
    private val repository: InTripCommunicationWebSocketRepository
) {
    suspend operator fun invoke(chatId: String): Flow<Message> {
        return repository.subscribeToChat(chatId)
    }
}