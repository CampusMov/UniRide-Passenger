package com.campusmov.uniride.data.datasource.remote.service

import com.campusmov.uniride.data.datasource.remote.dto.InfractionResponseDto
import com.campusmov.uniride.data.datasource.remote.dto.ValorationRequestDto
import com.campusmov.uniride.data.datasource.remote.dto.ValorationResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReputationIncentivesService {
    @GET("reputation-incentives-service/reputation-incentives/valorations/{userId}")
    suspend fun getAllValorationsByUserId(@Path ("userId") userId: String): Response<List<ValorationResponseDto>>

    @GET("reputation-incentives-service/reputation-incentives/infractions/{userId}")
    suspend fun getAllPenaltiesByUserId(@Path ("userId") userId: String): Response<List<InfractionResponseDto>>

    @POST("reputation-incentives-service/reputation-incentives/valorations")
    suspend fun createValoration(
        @Body  valorationRequestDto: ValorationRequestDto
    ): Response<ValorationResponseDto>

}