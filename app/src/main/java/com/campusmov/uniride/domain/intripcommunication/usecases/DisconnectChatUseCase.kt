package com.campusmov.uniride.domain.intripcommunication.usecases

import com.campusmov.uniride.data.datasource.remote.service.InTripSocketService
import javax.inject.Inject

class DisconnectChatUseCase @Inject constructor(
    private val socketService: InTripSocketService
) {
    operator fun invoke() {
        socketService.cleanup()
    }
}