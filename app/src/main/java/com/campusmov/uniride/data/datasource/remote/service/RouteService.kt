package com.campusmov.uniride.data.datasource.remote.service

import com.campusmov.uniride.data.datasource.remote.dto.RouteRequestDto
import com.campusmov.uniride.data.datasource.remote.dto.RouteResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface RouteService {
    @POST("matching-routing-service/routes/shortest/a-star")
    suspend fun getRoute(
        @Body request: RouteRequestDto
    ): RouteResponseDto
}