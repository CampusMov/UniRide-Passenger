package com.campusmov.uniride.domain.auth.repository

import com.campusmov.uniride.domain.auth.model.AuthEmailVerificationResponse
import com.campusmov.uniride.domain.shared.util.Resource

interface AuthRepository {
    suspend fun verifyEmail(email: String): Resource<AuthEmailVerificationResponse>
}