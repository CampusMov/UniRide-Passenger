package com.campusmov.uniride.domain.auth.repository

import com.campusmov.uniride.domain.auth.model.AuthVerificationCodeResponse
import com.campusmov.uniride.domain.shared.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun verifyEmail(email: String): Resource<Unit>
    suspend fun verifyCode(code: String, role: String): Resource<AuthVerificationCodeResponse>
    suspend fun saveSession(authVerificationCodeResponse: AuthVerificationCodeResponse)
    fun getSessionData(): Flow<AuthVerificationCodeResponse>
}