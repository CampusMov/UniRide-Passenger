package com.campusmov.uniride.domain.auth.usecases

import com.campusmov.uniride.domain.auth.repository.UserRepository

class GetUserByEmailLocallyUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String) = userRepository.getUserByEmailLocally(email)
}