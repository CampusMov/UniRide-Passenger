package com.campusmov.uniride.domain.auth.usecases

import com.campusmov.uniride.domain.auth.repository.UserRepository

class GetUserByIdLocallyUserCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String) = userRepository.getUserByIdLocally(userId)
}