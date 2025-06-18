package com.campusmov.uniride.data.datasource.remote.service

import com.campusmov.uniride.data.datasource.remote.dto.ValorationResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ReputationIncentivesService {
    @GET("reputation-incentives/valorations/{userId}")
    suspend fun getAllValorationsByUserId(@Path ("userId") userId: String): Response<List<ValorationResponseDto>>
}