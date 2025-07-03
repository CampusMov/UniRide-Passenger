package com.campusmov.uniride.domain.intripcommunication.usecases

import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationRepository
import com.campusmov.uniride.domain.shared.util.Resource

class CloseChatUseCase(
    private val repository: InTripCommunicationRepository
) {
    suspend operator fun invoke(chatId: String): Resource<Unit> =
        repository.closeChat(chatId)
}