package com.campusmov.uniride.presentation.navigation.graph.routingmatching

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.campusmov.uniride.presentation.navigation.Graph
import com.campusmov.uniride.presentation.navigation.screen.routingmatching.RoutingMatchingScreen
import com.campusmov.uniride.presentation.views.routingmatching.mapcontent.MapCarpoolSearcherView

//@Composable
fun NavGraphBuilder.RoutingMatchingNavGraph(navHostController: NavHostController) {
    navigation(
        route = Graph.HOME,
        startDestination = RoutingMatchingScreen.SearchCarpool.route
    ){
        composable(route = RoutingMatchingScreen.SearchCarpool.route) {
            MapCarpoolSearcherView(navHostController = navHostController)
        }
        composable(route = RoutingMatchingScreen.AddPickUpPoint.route) {
            // TODO("Implement the Carpool Details Screen")
        }
    }

    /*
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
    }*/
}