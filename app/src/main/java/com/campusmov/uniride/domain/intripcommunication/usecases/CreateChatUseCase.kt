package com.campusmov.uniride.domain.intripcommunication.usecases

import com.campusmov.uniride.domain.intripcommunication.model.Chat
import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationRepository
import com.campusmov.uniride.domain.shared.util.Resource

class CreateChatUseCase(
    private val repository: InTripCommunicationRepository
) {
    suspend operator fun invoke(
        carpoolId: String,
        driverId: String,
        passengerId: String
    ): Resource<Chat> = repository.createChat(carpoolId, driverId, passengerId)
}