package com.campusmov.uniride.presentation.navigation.graph.profile

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.campusmov.uniride.presentation.navigation.Graph
import com.campusmov.uniride.presentation.navigation.screen.profile.ProfileScreen
import com.campusmov.uniride.presentation.views.profile.info.ProfileInfoView
import com.campusmov.uniride.presentation.views.profile.registerprofile.RegisterProfileViewModel
import com.campusmov.uniride.presentation.views.profile.registerprofile.components.RegisterProfileAcademicInformationView
import com.campusmov.uniride.presentation.views.profile.registerprofile.components.RegisterProfileAcceptTermsView
import com.campusmov.uniride.presentation.views.profile.registerprofile.components.RegisterProfileContactInformationView
import com.campusmov.uniride.presentation.views.profile.registerprofile.components.RegisterProfileFullNameView
import com.campusmov.uniride.presentation.views.profile.registerprofile.components.RegisterProfileListSectionsView
import com.campusmov.uniride.presentation.views.profile.registerprofile.components.RegisterProfilePersonalInformationView

@SuppressLint("UnrememberedGetBackStackEntry")
@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.ProfileNavGraph(navHostController: NavHostController) {
    navigation(
        route = Graph.PROFILE,
        startDestination = ProfileScreen.RegisterProfileFullName.route
    ) {
        composable(route = ProfileScreen.RegisterProfileFullName.route) { backStackEntry ->
            val parentEntry = navHostController.getBackStackEntry(Graph.PROFILE)
            val viewModel: RegisterProfileViewModel = hiltViewModel(parentEntry)
            RegisterProfileFullNameView(viewModel = viewModel, navHostController = navHostController)
        }
        composable(route = ProfileScreen.RegisterProfileListItems.route) { backStackEntry ->
            val parentEntry = navHostController.getBackStackEntry(Graph.PROFILE)
            val viewModel: RegisterProfileViewModel = hiltViewModel(parentEntry)
            RegisterProfileListSectionsView( viewModel = viewModel, navHostController = navHostController)
        }
        composable(route = ProfileScreen.RegisterProfilePersonalInfo.route) { backStackEntry ->
            val parentEntry = navHostController.getBackStackEntry(Graph.PROFILE)
            val viewModel: RegisterProfileViewModel = hiltViewModel(parentEntry)
            RegisterProfilePersonalInformationView(viewModel = viewModel, navHostController = navHostController)
        }
        composable(route = ProfileScreen.RegisterProfileContactInfo.route) { backStackEntry ->
            val parentEntry = navHostController.getBackStackEntry(Graph.PROFILE)
            val viewModel: RegisterProfileViewModel = hiltViewModel(parentEntry)
            RegisterProfileContactInformationView(viewModel = viewModel, navHostController = navHostController)
        }
        composable(route = ProfileScreen.RegisterProfileAcademicInfo.route) { backStackEntry ->
            val parentEntry = navHostController.getBackStackEntry(Graph.PROFILE)
            val viewModel: RegisterProfileViewModel = hiltViewModel(parentEntry)
            RegisterProfileAcademicInformationView(viewModel = viewModel, navHostController = navHostController)
        }
        composable(route = ProfileScreen.RegisterProfileAcceptTerms.route) { backStackEntry ->
            val parentEntry = navHostController.getBackStackEntry(Graph.PROFILE)
            val viewModel: RegisterProfileViewModel = hiltViewModel(parentEntry)
            RegisterProfileAcceptTermsView(viewModel = viewModel, navHostController = navHostController)
        }
        composable(route = ProfileScreen.ProfileInfo.route) {
            ProfileInfoView(navHostController = navHostController)
        }
    }
}