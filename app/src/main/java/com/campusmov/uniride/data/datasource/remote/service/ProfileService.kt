package com.campusmov.uniride.data.datasource.remote.service

import com.campusmov.uniride.data.datasource.remote.dto.ProfileRequestDto
import com.campusmov.uniride.data.datasource.remote.dto.ProfileResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ProfileService {
    @POST("profiles")
    suspend fun saveProfile(
        @Body profileRequest: ProfileRequestDto
    ): Response<Unit>
}