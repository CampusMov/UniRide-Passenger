package com.campusmov.uniride.presentation.navigation.graph.analytic

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.campusmov.uniride.presentation.navigation.Graph
import com.campusmov.uniride.presentation.navigation.screen.analytic.AnalyticsScreen
import com.campusmov.uniride.presentation.views.analytics.studentRating.StudentRatingMetricsView

fun NavGraphBuilder.AnalyticsNavGraph(navHostController: NavHostController) {
    navigation(
        route = Graph.ANALYTICS,
        startDestination = AnalyticsScreen.StudentRatingMetrics.route
    ) {
        composable(route = AnalyticsScreen.StudentRatingMetrics.route){
            StudentRatingMetricsView(navHostController = navHostController)
        }
    }
}