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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.R
import com.campusmov.uniride.domain.shared.model.EUserCarpoolState
import com.campusmov.uniride.presentation.views.routingmatching.carpoolssearchresults.CarpoolsSearchResultsView
import com.campusmov.uniride.presentation.views.routingmatching.mapcontent.components.GoogleMapContent
import com.campusmov.uniride.presentation.views.routingmatching.searchcarpool.SearchCarpoolView
import com.campusmov.uniride.presentation.views.routingmatching.searchclassschedule.SearchClassScheduleView
import com.campusmov.uniride.presentation.views.routingmatching.searchplace.SearchPlaceView
import com.campusmov.uniride.presentation.views.routingmatching.waitforcarpoolstart.WaitForCarpoolStart

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapCarpoolSearcherView(
    viewModel: MapContentViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val userCarpoolSate = viewModel.userCarpoolState.collectAsState()

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

    var showCarpoolsSearchResults = remember {
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
                        if (userCarpoolSate.value == EUserCarpoolState.SEARCHING) {
                            SearchCarpoolView(
                                navHostController = navHostController,
                                onPickUpPointSelected = {
                                    showSearchPickUpPoint.value = true
                                },
                                onClassScheduleSelected = {
                                    showSearchClassSchedule.value = true
                                },
                                onOpenCarpoolsSearchResultsView = {
                                    showCarpoolsSearchResults.value = true
                                },
                            )
                        }
                        if (userCarpoolSate.value == EUserCarpoolState.WAITING_FOR_CARPOOL_START) {
                            WaitForCarpoolStart(navHostController = navHostController)
                        }
                        if (userCarpoolSate.value == EUserCarpoolState.IN_CARPOOL) {
                            // TODO: Implement the In Carpool state UI
                        }
                        if (userCarpoolSate.value == EUserCarpoolState.COMPLETED) {
                            // TODO: Implement the Completed state UI
                        }
                        if (userCarpoolSate.value == EUserCarpoolState.CANCELLED) {
                            // TODO: Implement the Cancelled state UI
                        }
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
                        if (!viewModel.isInteractiveWithMap.value){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(paddingValues)
                                    .padding(horizontal = 20.dp, vertical = 10.dp)
                                    .offset(y = calculateSearchCarpoolHeight()),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(
                                    onClick = {
                                        showCarpoolsSearchResults.value = true
                                    },
                                    modifier = Modifier
                                        .size(55.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(Color(0xFF262626))
                                    ) {
                                        Image(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(10.dp)
                                                .align(Alignment.Center),
                                            painter = painterResource(id = R.drawable.front_car),
                                            contentDescription = "Icon of results carpools",
                                        )
                                    }
                                }
                            }
                        }
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
                        if (showCarpoolsSearchResults.value) {
                            CarpoolsSearchResultsView(
                                navHostController = navHostController,
                                onGoBack = {
                                    showCarpoolsSearchResults.value = false
                                }
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

@Composable
private fun calculateSearchCarpoolHeight(): Dp {
    val heightEnlargedPercentage = 0.52f
    return LocalConfiguration.current.screenHeightDp.dp * heightEnlargedPercentage
}