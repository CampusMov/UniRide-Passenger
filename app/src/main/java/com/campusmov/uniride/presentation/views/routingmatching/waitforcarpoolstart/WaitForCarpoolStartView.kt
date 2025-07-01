package com.campusmov.uniride.presentation.views.routingmatching.waitforcarpoolstart

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.presentation.views.routingmatching.mapcontent.MapContentViewModel

@Composable
fun WaitForCarpoolStartView(
    navHostController: NavHostController,
    viewModel: WaitForCarpoolStartViewModel = hiltViewModel(),
    viewModelMapContent: MapContentViewModel = hiltViewModel(),
) {
    val isLoading = viewModel.isLoading.collectAsState()
    val currentCarpool = viewModel.currentCarpool.collectAsState()

    LaunchedEffect(viewModelMapContent.carpoolAcceptedId.value) {
        if (viewModelMapContent.carpoolAcceptedId.value?.isNotEmpty() == true) {
            Log.d("TAG", "Carpool ID: ${viewModelMapContent.carpoolAcceptedId.value}")
            viewModel.getCarpoolById(viewModelMapContent.carpoolAcceptedId.value!!)
        } else {
            Log.d("TAG", "No carpool ID found")
        }
    }

    Log.d("TAG", "Carpool ID : ${viewModelMapContent.carpoolAcceptedId.value}")
    Scaffold { paddingValues ->
        Column {
            if(!isLoading.value) {
                Text(
                    text = "Waiting for carpool to start: ${viewModelMapContent.carpoolAcceptedId.value}",
                    modifier = Modifier
                        .padding(paddingValues),
                )
                Text(
                    text = "Current Carpool: ${viewModel.currentCarpool.value?.id?: "No carpool found"}",
                    modifier = Modifier.padding(paddingValues)
                )
                Text(
                    text = "Current Carpool: ${viewModel.currentCarpool.value?.origin?.longitude ?: "No origin longitude"}",
                    modifier = Modifier.padding(paddingValues)
                )
            } else {
                Text(
                    text = "Loading carpool data...",
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}