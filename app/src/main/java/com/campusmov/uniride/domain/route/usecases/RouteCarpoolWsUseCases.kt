package com.campusmov.uniride.domain.route.usecases

data class RouteCarpoolWsUseCases(
    val connectRouteCarpoolUseCase: ConnectRouteCarpoolUseCase,
    val subscribeRouteCarpoolUpdatesUseCase: SubscribeRouteCarpoolUpdateCurrentLocationUseCase,
    val disconnectRouteCarpoolUseCase: DisconnectRouteCarpoolSessionUseCase
)
