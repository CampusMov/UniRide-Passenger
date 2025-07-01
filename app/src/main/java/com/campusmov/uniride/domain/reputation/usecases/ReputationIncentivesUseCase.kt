package com.campusmov.uniride.domain.reputation.usecases

data class ReputationIncentivesUseCase(
    val getValorationsOfUser: ValorationUseCase,
    val getInfractionsOfUser: InfractionUseCase
)