package com.campusmov.uniride.presentation.navigation.graph.reputation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.campusmov.uniride.presentation.navigation.Graph
import com.campusmov.uniride.presentation.navigation.screen.reputation.ReputationScreen
import com.campusmov.uniride.presentation.views.reputation.infractions.IncidentsView

fun NavGraphBuilder.ReputationNavGraph(navHostController: NavHostController) {
    navigation(
        route = Graph.REPUTATION,
        startDestination = ReputationScreen.infractions.route
    ){
        composable(route = ReputationScreen.infractions.route) {
            IncidentsView(navHostController = navHostController)
        }
    }
}