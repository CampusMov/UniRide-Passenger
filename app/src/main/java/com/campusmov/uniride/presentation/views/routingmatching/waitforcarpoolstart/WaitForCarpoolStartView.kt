package com.campusmov.uniride.presentation.views.routingmatching.waitforcarpoolstart

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.presentation.views.routingmatching.mapcontent.MapContentViewModel

@Composable
fun WaitForCarpoolStart(
    navHostController: NavHostController,
    viewModelMapContent: MapContentViewModel = hiltViewModel(),
) {
    Scaffold { paddingValues ->
        Text(
            text = "Waiting for carpool to start: ${viewModelMapContent.carpoolAcceptedId.value}",
            modifier = Modifier
                .padding(paddingValues)
        )
    }
}