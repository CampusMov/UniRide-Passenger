package com.campusmov.uniride.domain.auth.usecases

import com.campusmov.uniride.domain.auth.repository.UserRepository

class GetUserLocallyUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.getUserLocally()
}