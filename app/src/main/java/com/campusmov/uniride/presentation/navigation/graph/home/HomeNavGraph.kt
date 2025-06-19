package com.campusmov.uniride.presentation.navigation.graph.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.campusmov.uniride.presentation.navigation.Graph
import com.campusmov.uniride.presentation.navigation.screen.home.HomeScreen
import com.campusmov.uniride.presentation.views.routingmatching.menunavigation.MenuNavigationView

fun NavGraphBuilder.HomeNavGraph(navHostController: NavHostController){
    navigation(
        route = Graph.HOME,
        startDestination = HomeScreen.Home.route
    ) {
        composable(route = HomeScreen.Home.route) {
            //MenuNavigationView()
        }
        composable(route = Graph.MATCHING) {
            //MenuNavigationView()
        }
    }
}