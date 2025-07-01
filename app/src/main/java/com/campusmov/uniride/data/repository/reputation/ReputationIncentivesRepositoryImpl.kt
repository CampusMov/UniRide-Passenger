package com.campusmov.uniride.data.repository.reputation

import com.campusmov.uniride.data.datasource.remote.service.ReputationIncentivesService
import com.campusmov.uniride.domain.reputation.model.Infraction
import com.campusmov.uniride.domain.reputation.model.Valoration
import com.campusmov.uniride.domain.reputation.repository.ReputationIncentivesRepository
import com.campusmov.uniride.domain.shared.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReputationIncentivesRepositoryImpl(private val reputationIncentivesService: ReputationIncentivesService): ReputationIncentivesRepository {
    override suspend fun getValorationsOfUser(userId: String): Resource<List<Valoration>> = withContext(Dispatchers.IO) {
        try {
            val response = reputationIncentivesService.getAllValorationsByUserId(userId)
            if (response.isSuccessful) {
                val valorations = response.body() ?: emptyList()
                if (valorations.isEmpty()) {
                    Resource.Failure("No valorations found for user ID: $userId")
                } else {
                    Resource.Success(valorations.map { it.toDomain() })
                }
            } else {
                Resource.Failure("Error fetching valorations: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Resource.Failure("Exception occurred while fetching valorations: ${e.message ?: "Unknown error"}")
        }
    }

    override suspend fun getInfractionsOfUser(userId: String): Resource<List<Infraction>> = withContext(Dispatchers.IO){
        try {
            val response = reputationIncentivesService.getAllPenaltiesByUserId(userId)
            if (response.isSuccessful) {
                val infractions = response.body() ?: emptyList()
                if (infractions.isEmpty()) {
                    Resource.Failure("No infractions found for user ID: $userId")
                } else {
                    Resource.Success(infractions.map { it.toDomain() })
                }
            } else {
                Resource.Failure("Error fetching infractions: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Resource.Failure("Exception occurred while fetching infractions: ${e.message ?: "Unknown error"}")
        }
    }
}