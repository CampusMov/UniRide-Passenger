package com.campusmov.uniride.presentation.views.routingmatching.mapcontent.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.campusmov.uniride.R
import com.campusmov.uniride.presentation.views.routingmatching.mapcontent.MapContentViewModel
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

@Composable
fun GoogleMapContent(
    navHostController: NavHostController,
    viewModel: MapContentViewModel,
    paddingValues: PaddingValues
){
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

    LaunchedEffect(key1 = location) {
        if (location.value != null && !isCameraCentered.value) {
            cameraPositionState.position = CameraPosition.Builder()
                .target(LatLng(location.value!!.latitude, location.value!!.longitude))
                .zoom(14f)
                .build()
            isCameraCentered.value = true
        }
    }

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