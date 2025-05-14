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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.collectAsState
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
import com.campusmov.uniride.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapCarpoolSearcherView(
    viewModel: MapContentViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val context = LocalContext.current

    val location = viewModel.location.collectAsState()
    val cameraPositionState = rememberCameraPositionState()
    var isCameraCentered = remember {
        mutableStateOf(false)
    }

    val mapProperties = remember {
        mutableStateOf(
            MapProperties(
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style),
                isMyLocationEnabled = true
            )
        )
    }

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

    LaunchedEffect(hasPermission.value) {
        if (!hasPermission.value) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            viewModel.startLocationUpdates()
        }
    }

    LaunchedEffect(key1 = location) {
        if (location.value != null && !isCameraCentered.value) {
            cameraPositionState.position = CameraPosition.Builder()
                .target(LatLng(location.value!!.latitude, location.value!!.longitude))
                .zoom(14f)
                .build()
            isCameraCentered.value = true
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
                        GoogleMap(
                            modifier = Modifier
                                .fillMaxHeight(
                                    fraction = if (viewModel.isInteractiveWithMap.value) 1f else 0.7f
                                )
                                .padding(paddingValues),
                            cameraPositionState = cameraPositionState,
                            properties = mapProperties.value,
                            uiSettings = MapUiSettings(
                                zoomControlsEnabled = false,
                                zoomGesturesEnabled = true
                            )
                        ) {
                            location.let { position ->
                                if (position.value != null) {
                                    Marker(
                                        state = MarkerState(position = position.value!!),
                                    )
                                }
                            }
                        }
                        CheckForMapInteraction(cameraPositionState = cameraPositionState, viewmodel = viewModel)
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

@Composable
private fun CheckForMapInteraction(
    cameraPositionState: CameraPositionState,
    viewmodel: MapContentViewModel
) {
    var initialCameraPosition = remember {
        mutableStateOf(cameraPositionState.position)
    }

    val onMapCameraMoveStart: (cameraPosition: CameraPosition) -> Unit = { newPosition ->
        initialCameraPosition.value = newPosition
        viewmodel.isInteractiveWithMap.value = true
    }

    val onMapCameraIdle: (cameraPosition: CameraPosition) -> Unit = { newCameraPosition ->
        val cameraMovementReason = cameraPositionState.cameraMoveStartedReason
        if (newCameraPosition.zoom < initialCameraPosition.value.zoom) {
            viewmodel.isInteractiveWithMap.value = false
        }

        if (newCameraPosition.zoom > initialCameraPosition.value.zoom) {
            viewmodel.isInteractiveWithMap.value = false
        }

        if (newCameraPosition.bearing != initialCameraPosition.value.bearing) {
            viewmodel.isInteractiveWithMap.value = false
        }

        if (cameraMovementReason == CameraMoveStartedReason.GESTURE) {
            if (newCameraPosition.target != initialCameraPosition.value.target) {
                viewmodel.isInteractiveWithMap.value = false
            }
        }
        initialCameraPosition.value = newCameraPosition
    }

    LaunchedEffect(key1 = cameraPositionState.isMoving) {
        if (cameraPositionState.isMoving) {
            onMapCameraMoveStart(cameraPositionState.position)
        } else {
            onMapCameraIdle(cameraPositionState.position)
        }
    }

}