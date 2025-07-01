package com.campusmov.uniride.domain.intripcommunication.usecases

import com.campusmov.uniride.domain.intripcommunication.model.Chat
import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationRepository
import com.campusmov.uniride.domain.shared.util.Resource

class GetPassengerChatUseCase(
    private val repository: InTripCommunicationRepository
) {
    suspend operator fun invoke(
        passengerId: String,
        carpoolId: String
    ): Resource<Chat> = repository.getPassengerChat(passengerId, carpoolId)
}