package com.campusmov.uniride.presentation.views.auth.verifiycode

data class VerificationCodeValidationState (
    val code: String = "",
    val email: String = "",
    val roles: List<String> = emptyList(),
)