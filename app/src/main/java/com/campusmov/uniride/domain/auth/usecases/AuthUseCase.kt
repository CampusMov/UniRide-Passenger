package com.campusmov.uniride.domain.auth.usecases

data class AuthUseCase (
    val verifyEmail: VerificationEmailUseCase,
    val verifyCode: VerificationCodeUseCase,
    val getUserById: GetUserByIdUseCase,
)