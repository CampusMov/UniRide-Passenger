package com.campusmov.uniride.domain.auth.usecases

import com.campusmov.uniride.domain.auth.repository.AuthRepository

class VerificationEmailUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String) = repository.verifyEmail(email)
}