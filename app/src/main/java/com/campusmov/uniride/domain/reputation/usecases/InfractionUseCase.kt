package com.campusmov.uniride.domain.reputation.usecases

import com.campusmov.uniride.domain.reputation.repository.ReputationIncentivesRepository

class InfractionUseCase(private val repository: ReputationIncentivesRepository) {
    suspend operator fun invoke(userId: String) = repository.getInfractionsOfUser(userId)
}