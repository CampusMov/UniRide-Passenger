package com.campusmov.uniride.domain.routingmatching.usecases

data class CarpoolWsUseCases(
    val connectCarpoolUseCase: ConnectCarpoolUseCase,
    val subscribeCarpoolStatusUpdatesUseCase: SubscribeCarpoolStatusUpdatesUseCase,
    val disconnectCarpoolUseCase: DisconnectCarpoolUseCase
)
