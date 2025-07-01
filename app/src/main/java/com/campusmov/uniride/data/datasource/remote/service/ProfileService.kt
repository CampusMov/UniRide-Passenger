package com.campusmov.uniride.data.datasource.remote.service

import com.campusmov.uniride.data.datasource.remote.dto.ProfileRequestDto
import com.campusmov.uniride.data.datasource.remote.dto.ProfileResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfileService {
    @POST("profile-service/profiles")
    suspend fun saveProfile(
        @Body profileRequest: ProfileRequestDto
    ): Response<Unit>

    @GET("profile-service/profiles/{id}")
    suspend fun  getProfileById(@Path ("id") profileId: String): Response<ProfileResponseDto>

}