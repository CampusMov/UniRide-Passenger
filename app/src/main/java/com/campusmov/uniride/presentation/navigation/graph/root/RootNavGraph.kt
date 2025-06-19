package com.campusmov.uniride.presentation.navigation.graph.root

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.campusmov.uniride.presentation.navigation.Graph
import com.campusmov.uniride.presentation.navigation.graph.analytic.AnalyticsNavGraph
import com.campusmov.uniride.presentation.navigation.graph.auth.AuthNavGraph
import com.campusmov.uniride.presentation.navigation.graph.home.HomeNavGraph
import com.campusmov.uniride.presentation.navigation.graph.profile.ProfileNavGraph
import com.campusmov.uniride.presentation.navigation.graph.routingmatching.RoutingMatchingNavGraph

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootNavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = Graph.AUTH
    ) {
        AuthNavGraph(navHostController)
        ProfileNavGraph(navHostController)
        RoutingMatchingNavGraph(navHostController)
        AnalyticsNavGraph(navHostController)
    }
}