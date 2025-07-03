package com.campusmov.uniride.domain.intripcommunication.usecases

import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationWebSocketRepository
import javax.inject.Inject

class DisconnectChatSessionUseCase @Inject constructor(
    private val repository: InTripCommunicationWebSocketRepository
) {
    suspend operator fun invoke() {
        repository.disconnectSession()
    }
}