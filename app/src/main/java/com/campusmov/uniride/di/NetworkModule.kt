package com.campusmov.uniride.di

import com.campusmov.uniride.core.Config
import com.campusmov.uniride.data.datasource.remote.service.AuthService
import com.campusmov.uniride.data.datasource.remote.service.ProfileClassScheduleService
import com.campusmov.uniride.data.datasource.remote.service.ProfileService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.MINUTES)
            .writeTimeout(30, TimeUnit.MINUTES)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    @DefaultRetrofit
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(Config.SERVICES_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(@DefaultRetrofit retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileService(@DefaultRetrofit retrofit: Retrofit): ProfileService {
        return retrofit.create(ProfileService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileClassScheduleService(@DefaultRetrofit retrofit: Retrofit): ProfileClassScheduleService {
        return retrofit.create(ProfileClassScheduleService::class.java)
    }
}