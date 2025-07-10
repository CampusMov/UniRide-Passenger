package com.campusmov.uniride.domain.routingmatching.usecases

data class PassengerRequestWsUseCases(
    val connectRequestsUseCase: ConnectRequestsUseCase,
    val subscribeRequestStatusUpdatesUseCase: SubscribeRequestStatusUpdatesUseCase,
    val disconnectRequestsUseCase: DisconnectRequestsUseCase
)