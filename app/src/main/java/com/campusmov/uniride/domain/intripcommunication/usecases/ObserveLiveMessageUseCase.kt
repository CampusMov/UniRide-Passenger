package com.campusmov.uniride.domain.intripcommunication.usecases

import com.campusmov.uniride.data.datasource.remote.service.InTripSocketService
import com.campusmov.uniride.domain.intripcommunication.model.Message
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class ObserveLiveMessagesUseCase @Inject constructor(
    private val socketService: InTripSocketService
) {
    operator fun invoke(): SharedFlow<Message> =
        socketService.incoming
}