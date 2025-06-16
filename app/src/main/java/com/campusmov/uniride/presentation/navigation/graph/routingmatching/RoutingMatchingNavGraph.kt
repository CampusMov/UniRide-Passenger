package com.campusmov.uniride.presentation.navigation.graph.routingmatching

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.campusmov.uniride.presentation.navigation.Graph
import com.campusmov.uniride.presentation.navigation.screen.routingmatching.RoutingMatchingScreen
import com.campusmov.uniride.presentation.views.routingmatching.mapcontent.MapCarpoolSearcherView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RoutingMatchingNavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        route = Graph.MATCHING,
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