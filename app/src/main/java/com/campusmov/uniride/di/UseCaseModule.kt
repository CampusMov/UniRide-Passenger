package com.campusmov.uniride.di

import com.campusmov.uniride.domain.auth.repository.AuthRepository
import com.campusmov.uniride.domain.auth.usecases.AuthUseCase
import com.campusmov.uniride.domain.auth.usecases.GetSessionDataUseCase
import com.campusmov.uniride.domain.auth.usecases.SaveSessionUseCase
import com.campusmov.uniride.domain.auth.usecases.VerificationCodeUseCase
import com.campusmov.uniride.domain.auth.usecases.VerificationEmailUseCase
import com.campusmov.uniride.domain.location.usecases.GetLocationsUpdatesUseCase
import com.campusmov.uniride.domain.location.usecases.LocationUsesCases
import com.campusmov.uniride.domain.routingmatching.repository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideAuthUseCase(authRepository: AuthRepository) = AuthUseCase(
        verifyEmail = VerificationEmailUseCase(authRepository),
        verifyCode = VerificationCodeUseCase(authRepository),
        saveSession = SaveSessionUseCase(authRepository),
        getSessionData = GetSessionDataUseCase(authRepository)
    )

    @Provides
    fun provideLocationUseCases(locationRepository: LocationRepository) = LocationUsesCases(
        getLocationUpdates = GetLocationsUpdatesUseCase(locationRepository)
    )
}