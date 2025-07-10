package com.campusmov.uniride.domain.intripcommunication.usecases

import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationRepository
import com.campusmov.uniride.domain.shared.util.Resource

class MarkMessageAsReadUseCase(
    private val repository: InTripCommunicationRepository
) {
    suspend operator fun invoke(
        chatId: String,
        messageId: String,
        readerId: String
    ): Resource<Unit> = repository.markMessageAsRead(chatId, messageId, readerId)
}
