package com.campusmov.uniride.presentation.navigation.screen.home

sealed class HomeScreen(val route: String) {
    object Home : HomeScreen("home")
}