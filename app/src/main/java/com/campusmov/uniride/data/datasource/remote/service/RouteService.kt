package com.campusmov.uniride.data.datasource.remote.service

import com.campusmov.uniride.data.datasource.remote.dto.RouteRequestDto
import com.campusmov.uniride.data.datasource.remote.dto.RouteResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface RouteService {
    @POST("routes/shortest/dijkstra")
    suspend fun getRouteDijkstra(
        @Body request: RouteRequestDto
    ): RouteResponseDto

    @POST("routes/shortest/a-star")
    suspend fun getRouteAStar(
        @Body request: RouteRequestDto
    ): RouteResponseDto
}