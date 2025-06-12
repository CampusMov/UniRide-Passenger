package com.campusmov.uniride.domain.auth.usecases

import com.campusmov.uniride.domain.auth.model.Role
import com.campusmov.uniride.domain.auth.repository.AuthRepository

class VerificationCodeUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(code: String, email: String, role: String = Role.PASSENGER.name) = repository.verifyCode(code, email, role)
}