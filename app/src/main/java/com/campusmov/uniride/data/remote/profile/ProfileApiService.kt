package com.campusmov.uniride.data.remote.profile

import com.campusmov.uniride.data.remote.dto.ProfileRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ProfileApiService {

    @FormUrlEncoded
    @POST("api/v1/profile")
    suspend fun saveProfile(
        @Body profileRequest: ProfileRequestDto
    ): Response<Unit>
}
