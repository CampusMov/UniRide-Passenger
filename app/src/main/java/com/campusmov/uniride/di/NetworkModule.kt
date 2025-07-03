package com.campusmov.uniride.di

import com.campusmov.uniride.core.Config
import com.campusmov.uniride.data.datasource.remote.service.AnalyticsService
import com.campusmov.uniride.data.datasource.remote.service.AuthService
import com.campusmov.uniride.data.datasource.remote.service.CarpoolService
import com.campusmov.uniride.data.datasource.remote.service.InTripCommunicationService
import com.campusmov.uniride.data.datasource.remote.service.PassengerRequestService
import com.campusmov.uniride.data.datasource.remote.service.ProfileClassScheduleService
import com.campusmov.uniride.data.datasource.remote.service.ProfileService
import com.campusmov.uniride.data.datasource.remote.service.ReputationIncentivesService
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
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

    @Provides
    @Singleton
    fun provideAnalyticsService(@DefaultRetrofit retrofit: Retrofit): AnalyticsService {
        return retrofit.create(AnalyticsService::class.java)
    }

    @Provides
    @Singleton
    fun provideCarpoolService(@DefaultRetrofit retrofit: Retrofit): CarpoolService {
        return retrofit.create(CarpoolService::class.java)
    }

    @Provides
    @Singleton
    fun provideReputationIncentivesService(@DefaultRetrofit retrofit: Retrofit): ReputationIncentivesService {
        return retrofit.create(ReputationIncentivesService::class.java)
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return Firebase.storage
    }

    @Provides
    @Singleton
    fun providePassengerRequestService(@DefaultRetrofit retrofit: Retrofit): PassengerRequestService {
        return retrofit.create(PassengerRequestService::class.java)
    }

    @Provides
    @Singleton
    fun provideInTripCommunicationService(@DefaultRetrofit retrofit: Retrofit): InTripCommunicationService {
        return retrofit.create(InTripCommunicationService::class.java)
    }
}
