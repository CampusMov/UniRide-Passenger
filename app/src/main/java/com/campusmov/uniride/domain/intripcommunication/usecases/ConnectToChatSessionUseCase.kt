package com.campusmov.uniride.domain.intripcommunication.usecases

import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationWebSocketRepository
import javax.inject.Inject

class ConnectToChatSessionUseCase @Inject constructor(
    private val repository: InTripCommunicationWebSocketRepository
) {
    suspend operator fun invoke() {
        repository.connectSession()
    }
}