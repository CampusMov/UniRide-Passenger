package com.campusmov.uniride.domain.auth.usecases

import com.campusmov.uniride.domain.auth.model.AuthVerificationCodeResponse
import com.campusmov.uniride.domain.auth.repository.AuthRepository

class SaveSessionUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke(authVerificationCodeResponse: AuthVerificationCodeResponse) = authRepository.saveSession(authVerificationCodeResponse)
}