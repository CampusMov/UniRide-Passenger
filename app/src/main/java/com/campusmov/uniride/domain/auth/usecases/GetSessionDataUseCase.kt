package com.campusmov.uniride.domain.auth.usecases

import com.campusmov.uniride.domain.auth.repository.AuthRepository

class GetSessionDataUseCase(private val authRepository: AuthRepository) {

    operator fun invoke() = authRepository.getSessionData()
}