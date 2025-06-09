package com.campusmov.uniride.domain.auth.usecases

import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.repository.UserRepository

class SaveUserLocallyUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User) = userRepository.saveUserLocally(user)
}
