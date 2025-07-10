package com.campusmov.uniride.presentation.navigation.graph.root

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.campusmov.uniride.presentation.navigation.Graph
import com.campusmov.uniride.presentation.navigation.graph.analytic.AnalyticsNavGraph
import com.campusmov.uniride.presentation.navigation.graph.auth.AuthNavGraph
import com.campusmov.uniride.presentation.navigation.graph.profile.ProfileNavGraph
import com.campusmov.uniride.presentation.navigation.graph.reputation.ReputationNavGraph
import com.campusmov.uniride.presentation.navigation.graph.routingmatching.RoutingMatchingNavGraph
import com.campusmov.uniride.presentation.views.SplashViewModel
import androidx.compose.runtime.getValue

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootNavGraph(navHostController: NavHostController) {
    val viewModel: SplashViewModel = hiltViewModel()
    val startDestination by viewModel.startDestination.collectAsState()
    if (startDestination != null) {
        NavHost(
            navController = navHostController,
            route = Graph.ROOT,
            startDestination = startDestination!!
        ) {
            AuthNavGraph(navHostController)
            ProfileNavGraph(navHostController)
            RoutingMatchingNavGraph(navHostController)
            AnalyticsNavGraph(navHostController)
            ReputationNavGraph(navHostController)
        }
    }
}