package com.campusmov.uniride.presentation.navigation.graph.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.campusmov.uniride.presentation.navigation.Graph
import com.campusmov.uniride.presentation.navigation.screen.auth.AuthScreen
import com.campusmov.uniride.presentation.views.auth.verifyemail.EnterInstitutionalEmailView
import com.campusmov.uniride.presentation.views.auth.WelcomeView
import com.campusmov.uniride.presentation.views.auth.verifiyCode.EnterVerificationCodeView

fun NavGraphBuilder.AuthNavGraph(navHostController: NavHostController){
    navigation(
        route = Graph.AUTH,
        startDestination = AuthScreen.Welcome.route
    ) {
        composable(route = AuthScreen.Welcome.route) {
            WelcomeView(navHostController)
        }
        composable(route = AuthScreen.EnterInstitutionalEmail.route) {
            EnterInstitutionalEmailView(navHostController = navHostController)
        }
        composable(route = AuthScreen.EnterVerificationCode.route) {
            EnterVerificationCodeView(navHostController = navHostController)
        }
        composable(route = AuthScreen.AcceptTermsAndConditions.route) {
            // TODO: Implement AcceptTermsAndConditionsView
        }
    }
}