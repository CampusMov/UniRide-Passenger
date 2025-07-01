package com.campusmov.uniride.data.datasource.remote.service

import com.campusmov.uniride.data.datasource.remote.dto.CarpoolResponseDto
import com.campusmov.uniride.data.datasource.remote.dto.SearchAvailableCarpoolsRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CarpoolService {
    @POST("matching-routing-service/carpools/available")
    suspend fun searchAvailableCarpools(
        @Body searchAvailableCarpoolsRequestDto: SearchAvailableCarpoolsRequestDto
    ): List<CarpoolResponseDto>

    @GET("matching-routing-service/carpools/{carpoolId}")
    suspend fun getCarpoolById(@Path("carpoolId") carpoolId: String): Response<CarpoolResponseDto>

}