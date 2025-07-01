package com.campusmov.uniride.domain.intripcommunication.usecases

import com.campusmov.uniride.domain.intripcommunication.model.Message
import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationRepository
import com.campusmov.uniride.domain.shared.util.Resource

class GetMessagesUseCase(
    private val repository: InTripCommunicationRepository
) {
    suspend operator fun invoke(chatId: String): Resource<List<Message>> =
        repository.getMessages(chatId)
}