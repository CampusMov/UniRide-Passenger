package com.campusmov.uniride.di

import com.campusmov.uniride.domain.auth.repository.AuthRepository
import com.campusmov.uniride.domain.auth.usecases.AuthUseCase
import com.campusmov.uniride.domain.auth.usecases.VerificationEmailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideAuthUseCase(authRepository: AuthRepository) = AuthUseCase(
        verifyEmail = VerificationEmailUseCase(authRepository)
    )
}