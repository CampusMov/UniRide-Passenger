package com.campusmov.uniride.domain.auth.usecases

import com.campusmov.uniride.domain.auth.repository.UserRepository

class DeleteAllUsersLocallyUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.deleteAllUserLocally()
}