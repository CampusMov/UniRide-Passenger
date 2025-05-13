package com.campusmov.uniride.data.di

import com.campusmov.uniride.common.ApiConstants
import com.campusmov.uniride.data.remote.auth.AuthService
import com.campusmov.uniride.data.repository.auth.AuthRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object DataModule {

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private fun getAuthService(): AuthService {
        return retrofit.create(AuthService::class.java)

    }

    fun getAuthRepository(): AuthRepository {
        return AuthRepository(getAuthService())
    }

}