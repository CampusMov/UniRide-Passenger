package com.campusmov.uniride.domain.intripcommunication.usecases

import com.campusmov.uniride.data.datasource.remote.service.InTripSocketService
import javax.inject.Inject

class ConnectToChatUseCase @Inject constructor(
    private val socketService: InTripSocketService
) {
    operator fun invoke(chatId: String) {
        socketService.connect(chatId)
    }
}