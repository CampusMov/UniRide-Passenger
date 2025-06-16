package com.campusmov.uniride.data.datasource.remote.service

import com.campusmov.uniride.data.datasource.remote.dto.CarpoolResponseDto
import com.campusmov.uniride.data.datasource.remote.dto.SearchAvailableCarpoolsRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface CarpoolService {
    @POST("carpools/available")
    suspend fun searchAvailableCarpools(
        @Body searchAvailableCarpoolsRequestDto: SearchAvailableCarpoolsRequestDto
    ): List<CarpoolResponseDto>
}