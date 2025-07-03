package com.campusmov.uniride.presentation.views.routingmatching.carpoolinprogress

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.presentation.views.routingmatching.mapcontent.MapContentViewModel
import com.campusmov.uniride.presentation.views.routingmatching.waitforcarpoolstart.WaitForCarpoolStartViewModel

@Composable
fun CarpoolInProgressView(
    navHostController: NavHostController,
    viewModel: CarpoolInProgressViewModel = hiltViewModel(),
    viewModelMapContent: MapContentViewModel = hiltViewModel(),
    viewModelWaitForCarpoolStartViewModel: WaitForCarpoolStartViewModel = hiltViewModel(),
) {
    val currentCarpool = viewModelWaitForCarpoolStartViewModel.currentCarpool.collectAsState()
    val route = viewModelMapContent.route.collectAsState()

    LaunchedEffect(currentCarpool.value) {
        if (currentCarpool.value != null) {
            viewModelMapContent.loadRoute(
                startLatitude = currentCarpool.value?.origin?.latitude ?: 0.0,
                startLongitude = currentCarpool.value?.origin?.longitude ?: 0.0,
                endLatitude = currentCarpool.value?.destination?.latitude ?: 0.0,
                endLongitude = currentCarpool.value?.destination?.longitude ?: 0.0
            )
        }
    }
    Text(
        text = "Carpool In Progress View ${route.value?.totalDistance}",
        color = Color.Red
    )
}