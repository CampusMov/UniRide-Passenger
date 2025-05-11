package com.campusmov.uniride.presentation.navigation.screen.auth

sealed class AuthScreen(val route: String) {
    object Welcome : AuthScreen("/welcome")
    object EnterInstitutionalEmail : AuthScreen("/auth/enter_institutional_email")
    object EnterVerificationCode : AuthScreen("/auth/enter_verification_code")
    object AcceptTermsAndConditions : AuthScreen("/auth/accept_terms_and_conditions")
}