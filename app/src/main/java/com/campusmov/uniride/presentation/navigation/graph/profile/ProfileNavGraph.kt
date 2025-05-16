package com.campusmov.uniride.presentation.navigation.graph.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.campusmov.uniride.presentation.navigation.Graph
import com.campusmov.uniride.presentation.navigation.screen.profile.ProfileScreen
import com.campusmov.uniride.presentation.profile.view.*

fun NavGraphBuilder.ProfileNavGraph(navHostController: NavHostController) {
    navigation(
        route = Graph.PROFILE,
        startDestination = ProfileScreen.RegisterProfileFullName.route
    ) {
        // FullName
        composable(ProfileScreen.RegisterProfileFullName.route) {
            NameView(
                onBack = {navHostController.popBackStack()},
                onNext = { navHostController.navigate(ProfileScreen.RegisterProfileTerms.route) }
            )
        }

        // Terms
        composable(ProfileScreen.RegisterProfileTerms.route) {
            TermsView(
                onBack = { navHostController.popBackStack() },
                onNext = { navHostController.navigate(ProfileScreen.RegisterProfileListItems.route) }
            )
        }

        // ProfileList
        composable(ProfileScreen.RegisterProfileListItems.route) {
            ProfileListView(
                onNavigate = { dest -> navHostController.navigate(dest) },
                onFinish = {//TODO MANDAR A LA HOME VIEW
                    }
            )
        }

        // Personal Info
        composable(ProfileScreen.RegisterProfilePersonalInfo.route) {
            PersonalInfoView(
                onBack = { navHostController.navigate(ProfileScreen.RegisterProfileListItems.route) },
                onNext = { navHostController.navigate(ProfileScreen.RegisterProfileContactInfo.route) }
            )
        }

        // Contact info
        composable(ProfileScreen.RegisterProfileContactInfo.route) {
            ContactInfoView(
                onBack = { navHostController.navigate(ProfileScreen.RegisterProfileListItems.route) },
                onNext = { navHostController.navigate(ProfileScreen.RegisterProfileAcademicInfo.route) }
            )
        }

        // Academic information
        composable(ProfileScreen.RegisterProfileAcademicInfo.route) {
            AcademicInfoView(
                onBack = { navHostController.navigate(ProfileScreen.RegisterProfileListItems.route) },
                onNext = {navHostController.navigate(ProfileScreen.RegisterProfileListItems)}
            )
        }

        // ProfileInfo
        //TODO Profile Info View
        /*
        composable(ProfileScreen.ProfileInfo.route) {
            ProfileInfoView(
                onBack = { navHostController.popBackStack() },
                onNext = { /* navega a donde corresponda */ }
            )
        }
         */
    }
}
