package com.campusmov.uniride.presentation.views.routingmatching.mapcontent

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import com.campusmov.uniride.presentation.views.routingmatching.searchcarpool.SearchCarpoolView
import com.campusmov.uniride.presentation.views.routingmatching.searchclassschedule.SearchClassScheduleView
import com.campusmov.uniride.presentation.views.routingmatching.searchplace.SearchPlaceView

@RequiresApi(Build.VERSION_CODES.O)
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

    var showSearchPickUpPoint = remember {
        mutableStateOf(false)
    }

    var showSearchClassSchedule = remember {
        mutableStateOf(false)
    }

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
                        SearchCarpoolView(
                            navHostController = navHostController,
                            onPickUpPointSelected = {
                                showSearchPickUpPoint.value = true
                            },
                            onClassScheduleSelected = {
                                showSearchClassSchedule.value = true
                            }
                        )
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
                        if (showSearchPickUpPoint.value) {
                            SearchPlaceView(
                                navHostController = navHostController,
                                onDismissRequest = {
                                    showSearchPickUpPoint.value = false
                                },
                                onPlaceSelected = { ->
                                    showSearchPickUpPoint.value = false
                                }
                            )
                        }
                        if (showSearchClassSchedule.value) {
                            SearchClassScheduleView(
                                navHostController = navHostController,
                                onDismissRequest = {
                                    showSearchClassSchedule.value = false
                                },
                                onClassScheduleSelected = {
                                    showSearchClassSchedule.value = false
                                },
                            )
                        }
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
    val heightEnlargedPercentage = 0.4f
    val minimizeHeight = 60.dp
    val normalHeight = LocalConfiguration.current.screenHeightDp.dp * heightEnlargedPercentage
    return animateDpAsState(
        if (viewmodel.isInteractiveWithMap.value ) minimizeHeight else normalHeight,
        animationSpec = spring(stiffness = 300f)
    ).value
}