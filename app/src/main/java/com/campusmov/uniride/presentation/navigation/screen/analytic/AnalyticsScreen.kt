package com.campusmov.uniride.presentation.navigation.screen.analytic

sealed class AnalyticsScreen(val route: String) {
    object StudentRatingMetrics : AnalyticsScreen("analytics/rating_metrics")

}