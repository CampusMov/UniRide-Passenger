package com.campusmov.uniride.domain.auth.usecases

import com.campusmov.uniride.domain.auth.repository.AuthRepository

class GetUserByIdUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(userId: String) = repository.getUserById(userId)
}