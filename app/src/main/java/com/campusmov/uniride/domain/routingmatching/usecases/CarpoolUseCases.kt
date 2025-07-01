package com.campusmov.uniride.domain.routingmatching.usecases

data class CarpoolUseCases(
    val searchCarpoolsAvailable: SearchCarpoolsAvailableUseCase,
    val getCarpoolById: GetCarpoolByIdUseCase
)