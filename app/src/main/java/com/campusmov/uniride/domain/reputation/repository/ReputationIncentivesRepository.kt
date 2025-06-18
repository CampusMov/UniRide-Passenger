package com.campusmov.uniride.domain.reputation.repository

import com.campusmov.uniride.domain.reputation.model.Valoration
import com.campusmov.uniride.domain.shared.util.Resource

interface ReputationIncentivesRepository {
    suspend fun getValorationsOfUser(userId: String): Resource<List<Valoration>>
}