package com.campusmov.uniride.presentation.views.routingmatching.mapcontent

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import com.campusmov.uniride.presentation.views.intripcommunication.chat.ChatDialog
import com.campusmov.uniride.presentation.views.routingmatching.carpoolinprogress.CarpoolInProgressView
import com.campusmov.uniride.presentation.views.routingmatching.carpoolssearchresults.CarpoolsSearchResultsView
import com.campusmov.uniride.presentation.views.routingmatching.carpoolssearchresults.CarpoolsSearchResultsViewModel
import com.campusmov.uniride.presentation.views.routingmatching.completedCarpool.CompletedCarpoolView
import com.campusmov.uniride.presentation.views.routingmatching.completedCarpool.CompletedCarpoolViewModel
import com.campusmov.uniride.presentation.views.routingmatching.mapcontent.components.GoogleMapContent
import com.campusmov.uniride.presentation.views.routingmatching.searchcarpool.SearchCarpoolView
import com.campusmov.uniride.presentation.views.routingmatching.searchclassschedule.SearchClassScheduleView
import com.campusmov.uniride.presentation.views.routingmatching.searchplace.SearchPlaceView
import com.campusmov.uniride.presentation.views.routingmatching.waitforcarpoolstart.WaitForCarpoolStartView

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapCarpoolSearcherView(
    viewModel: MapContentViewModel = hiltViewModel(),
    viewModelCarpoolsSearchResultsViewModel: CarpoolsSearchResultsViewModel = hiltViewModel(),
    viewModelCarpoolCompleted: CompletedCarpoolViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val userCarpoolSate = viewModel.userCarpoolState.collectAsState()
    val showSearchPickUpPoint = viewModel.showSearchPickUpPoint
    val showSearchClassSchedule = viewModel.showSearchClassSchedule
    val showCarpoolsSearchResults = viewModel.showCarpoolsSearchResults
    val showChat = viewModel.showChat
    val currentCarpoolActive = viewModel.carpoolAcceptedId
    val user = viewModel.user.collectAsState()

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
            viewModelCarpoolsSearchResultsViewModel.connectToPassengerRequestWebSocket()
        }
    }

    LaunchedEffect(viewModelCarpoolsSearchResultsViewModel.passengerRequestAccepted.value){
        if (viewModelCarpoolsSearchResultsViewModel.passengerRequestAccepted.value != null) {
            viewModelCarpoolsSearchResultsViewModel.passengerRequestAccepted.value?.let { passengerRequest ->
                Log.d("TAG", "Carpool accepted: ${passengerRequest.carpoolId}")
                viewModel.showCarpoolsSearchResults.value = false
                viewModel.waitForCarpoolStart()
                viewModel.carpoolAcceptedId.value = passengerRequest.carpoolId
            }
        }
    }

    LaunchedEffect(viewModelCarpoolCompleted.carpoolFinished.value) {
        if(viewModelCarpoolCompleted.carpoolFinished.value == true){
            Log.d("TAG", "MapContentView ${viewModelCarpoolCompleted.carpoolFinished.value}")
            viewModel.searchCarpool()
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
                            .heightIn(max = calculateParentColumnHeight(viewmodel = viewModel))
                            .background(Color.Black)
                            .verticalScroll(rememberScrollState())
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
                            WaitForCarpoolStartView(navHostController = navHostController)
                        }
                        if (userCarpoolSate.value == EUserCarpoolState.IN_CARPOOL) {
                            CarpoolInProgressView(navHostController = navHostController)
                        }
                        if (userCarpoolSate.value == EUserCarpoolState.COMPLETED) {
                            Log.d("TAG", "carpool completed ${userCarpoolSate.value}")
                            CompletedCarpoolView(navHostController = navHostController)
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
                                if (userCarpoolSate.value == EUserCarpoolState.WAITING_FOR_CARPOOL_START
                                    || userCarpoolSate.value == EUserCarpoolState.IN_CARPOOL) {
                                    IconButton(
                                        onClick = {
                                            showChat.value = true
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
                                                painter = painterResource(id = R.drawable.chat_icon),
                                                contentDescription = "Icon of opening chat with driver",
                                            )
                                        }
                                    }
                                }
                                if (userCarpoolSate.value == EUserCarpoolState.SEARCHING) {
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
                        if (currentCarpoolActive.value != null && user.value?.id != null
                            && (userCarpoolSate.value == EUserCarpoolState.WAITING_FOR_CARPOOL_START || userCarpoolSate.value == EUserCarpoolState.IN_CARPOOL)
                        ) {
                            ChatDialog(
                                passengerId = user.value?.id ?: "",
                                carpoolId = currentCarpoolActive.value ?: "",
                                show = showChat.value,
                                onDismiss = {
                                    showChat.value = false
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

@Composable
private fun calculateParentColumnHeight(
    viewmodel: MapContentViewModel
) : Dp {
    val heightEnlargedPercentage = 0.3f
    val minimizeHeight = 60.dp
    val normalHeight = LocalConfiguration.current.screenHeightDp.dp * heightEnlargedPercentage
    return animateDpAsState(
        if (viewmodel.isInteractiveWithMap.value ) minimizeHeight else normalHeight,
        animationSpec = spring(stiffness = 300f)
    ).value
}