package com.campusmov.uniride.data.remote.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/v1/registration/log-email")

    suspend fun sendVerificationEmail(@Body request: LoginUserDto): Response<UserDto>
}