package com.campusmov.uniride.presentation.navigation.graph.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.campusmov.uniride.presentation.navigation.Graph
import com.campusmov.uniride.presentation.navigation.screen.profile.ProfileScreen

fun NavGraphBuilder.ProfileNavGraph(navHostController: NavHostController) {
    navigation(
        route = Graph.PROFILE,
        startDestination = ProfileScreen.RegisterProfileFullName.route
    ) {
        composable(route = ProfileScreen.RegisterProfileFullName.route) {
            // TODO: Implement the RegisterProfileNameView
        }
        composable(route = ProfileScreen.RegisterProfileListItems.route) {
            // TODO: Implement the RegisterProfileListItemsView
        }
        composable(route = ProfileScreen.RegisterProfilePersonalInfo.route) {
            // TODO: Implement the RegisterProfilePersonalInfoView
        }
        composable(route = ProfileScreen.RegisterProfileContactInfo.route) {
            // TODO: Implement the RegisterProfileContactInfoView
        }
        composable(route = ProfileScreen.RegisterProfileAcademicInfo.route) {
            // TODO: Implement the RegisterProfileAcademicInfoView
        }
    }
}