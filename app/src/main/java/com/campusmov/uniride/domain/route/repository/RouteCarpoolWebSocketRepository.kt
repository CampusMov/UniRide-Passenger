package com.campusmov.uniride.domain.route.repository

import com.campusmov.uniride.domain.route.model.RouteCarpool
import kotlinx.coroutines.flow.Flow

interface RouteCarpoolWebSocketRepository {
    suspend fun connectRouteCarpoolSession()
    suspend fun subscribeToRouteCarpoolUpdateCurrentLocation(carpoolId: String): Flow<RouteCarpool>
    suspend fun disconnectRouteCarpoolSession()
    suspend fun awaitConnectionReady()
}