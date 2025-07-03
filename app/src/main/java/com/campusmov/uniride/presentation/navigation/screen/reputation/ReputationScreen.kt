package com.campusmov.uniride.presentation.navigation.screen.reputation

sealed class ReputationScreen(val route: String) {
    object infractions : ReputationScreen("reputation/incidents")
}