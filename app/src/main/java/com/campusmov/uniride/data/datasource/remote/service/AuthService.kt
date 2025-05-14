package com.campusmov.uniride.data.datasource.remote.service

import com.campusmov.uniride.domain.auth.model.AuthVerificationCodeResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("api/v1/registration/sign-up")
    suspend fun sendVerificationEmail(
        @Field("email") email: String,
    ): Response<Unit>

    @FormUrlEncoded
    @POST("api/v1/login/verify-account")
    suspend fun sendVerificationCode(
        @Field("email") email: String,
        @Field("verificationCode") verificationCode: String,
        @Field("role") role: String
    ): Response<AuthVerificationCodeResponse>

}