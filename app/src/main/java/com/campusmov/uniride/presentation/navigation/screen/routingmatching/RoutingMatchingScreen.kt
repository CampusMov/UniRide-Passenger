package com.campusmov.uniride.presentation.navigation.screen.routingmatching

sealed class RoutingMatchingScreen(val route: String) {
    object Home: RoutingMatchingScreen("/home")
    object SearchCarpool: RoutingMatchingScreen("/search_carpool")
    object AddPickUpPoint: RoutingMatchingScreen("/search_carpool/add_pickup_point")
}