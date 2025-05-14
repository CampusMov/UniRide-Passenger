package com.campusmov.uniride.presentation.views.routingmatching.mapcontent

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.presentation.views.routingmatching.mapcontent.components.GoogleMapContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapCarpoolSearcherView(
    viewModel: MapContentViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val context = LocalContext.current

    val hasPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasPermission.value = isGranted
            if (isGranted) {
                viewModel.startLocationUpdates()
            }
        }
    )

    LaunchedEffect(Unit) {
        if (!hasPermission.value) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            viewModel.startLocationUpdates()
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars
    ) { paddingValues ->
        BottomSheetScaffold(
            sheetContentColor = Color.Black,
            sheetContainerColor = Color.Black,
            sheetSwipeEnabled = false,
            modifier = Modifier
                .fillMaxSize(),
            sheetContent = {
                AnimatedVisibility(visible = !viewModel.isInteractiveWithMap.value) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(calculateSheetHeight(viewmodel = viewModel))
                            .background(Color.Black)
                    ) {

                    }
                }
            },
            scaffoldState = rememberBottomSheetScaffoldState(),
            sheetPeekHeight = calculateSheetHeight(viewmodel = viewModel),
            content = {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (hasPermission.value) {
                        GoogleMapContent(
                            navHostController = navHostController,
                            viewModel = viewModel,
                            paddingValues = paddingValues
                        )
                    }
                    else {
                        Text(
                            text = "Location permission is required to use this feature.",
                            modifier = Modifier.padding(paddingValues)
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun calculateSheetHeight(
    viewmodel: MapContentViewModel
) : Dp {
    val height_enlarged_percentaje = 0.4f
    val minimize_height = 60.dp
    val normalHeight = LocalConfiguration.current.screenHeightDp.dp * height_enlarged_percentaje
    return animateDpAsState(
        if (viewmodel.isInteractiveWithMap.value ) minimize_height else normalHeight,
        animationSpec = spring(stiffness = 300f)
    ).value
}