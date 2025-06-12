package com.campusmov.uniride.domain.auth.repository

import com.campusmov.uniride.domain.auth.model.AuthVerificationCodeResponse
import com.campusmov.uniride.domain.shared.util.Resource

interface AuthRepository {
    suspend fun verifyEmail(email: String): Resource<Unit>
    suspend fun verifyCode(code: String, email: String, role: String): Resource<AuthVerificationCodeResponse>
}