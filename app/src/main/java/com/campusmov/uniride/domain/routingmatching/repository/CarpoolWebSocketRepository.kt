package com.campusmov.uniride.domain.routingmatching.repository

import com.campusmov.uniride.domain.routingmatching.model.Carpool
import kotlinx.coroutines.flow.Flow

interface CarpoolWebSocketRepository {
    suspend fun connectCarpoolSession()
    suspend fun subscribeToCarpoolStatus(carpoolId: String): Flow<Carpool>
    suspend fun disconnectCarpoolSession()
    suspend fun awaitConnectionReady()
}