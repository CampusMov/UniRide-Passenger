package com.campusmov.uniride.domain.reputation.usecases

import com.campusmov.uniride.domain.reputation.repository.ReputationIncentivesRepository

class ValorationUseCase(private val repository: ReputationIncentivesRepository) {
    suspend operator fun invoke(userId: String) = repository.getValorationsOfUser(userId)
    suspend operator fun invoke(driverId: String, userId: String ,rating: Int, message: String) = repository.createValoration(driverId, userId, rating, message)
}