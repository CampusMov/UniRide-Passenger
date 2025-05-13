package com.campusmov.uniride.presentation.navigation.graph.routingmatching

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.campusmov.uniride.presentation.navigation.Graph
import com.campusmov.uniride.presentation.navigation.screen.routingmatching.RoutingMatchingScreen
import com.campusmov.uniride.presentation.views.routingmatching.mapcarpoolsearcher.MapCarpoolSearcherView

@Composable
fun RoutingMatchingNavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        route = Graph.HOME,
        startDestination = RoutingMatchingScreen.SearchCarpool.route
    ) {
        composable(route = RoutingMatchingScreen.SearchCarpool.route) {
            MapCarpoolSearcherView(navHostController = navHostController)
        }
        composable(route = RoutingMatchingScreen.AddPickUpPoint.route) {
            // TODO("Implement the Carpool Details Screen")
        }
    }
}