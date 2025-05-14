package com.campusmov.uniride.domain.auth.usecases

import com.campusmov.uniride.domain.auth.repository.AuthRepository

class VerificationCodeUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(code: String, role: String) = repository.verifyCode(code, role)
}