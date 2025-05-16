package com.campusmov.uniride.presentation.navigation.graph.root

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.campusmov.uniride.presentation.navigation.Graph
import com.campusmov.uniride.presentation.navigation.graph.auth.AuthNavGraph
import com.campusmov.uniride.presentation.navigation.graph.profile.ProfileNavGraph

@Composable
fun RootNavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = Graph.AUTH
    ) {
        AuthNavGraph(navHostController)
        ProfileNavGraph(navHostController)
    }
}