package com.campusmov.uniride.domain.auth.usecases

data class AuthUseCase (
    val verifyEmail: VerificationEmailUseCase,
    val verifyCode: VerificationCodeUseCase,
    val saveSession: SaveSessionUseCase,
    val getSessionData: GetSessionDataUseCase
)