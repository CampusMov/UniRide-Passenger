package com.campusmov.uniride.presentation.views.auth.verifiyCode

data class VerificationCodeValidationState (
    val code: String = "",
    val email: String = "",
    val Roles: List<String> = emptyList(),
)