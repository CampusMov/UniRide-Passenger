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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.campusmov.uniride.R
import com.campusmov.uniride.presentation.views.routingmatching.mapcontent.MapContentViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
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
    val route = viewModel.route.collectAsState()
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

    LaunchedEffect(key1 = location.value, key2 = route.value) {
        if (!isCameraCentered.value) {
            route.value?.let { routeData ->
                if (routeData.intersections.isNotEmpty()) {
                    val boundsBuilder = LatLngBounds.Builder()
                    routeData.intersections.forEach { intersection ->
                        boundsBuilder.include(LatLng(intersection.latitude, intersection.longitude))
                    }

                    location.value?.let { userLocation ->
                        boundsBuilder.include(userLocation)
                    }

                    val bounds = boundsBuilder.build()
                    val padding = 100

                    try {
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLngBounds(bounds, padding),
                            durationMs = 1000
                        )
                        isCameraCentered.value = true
                    } catch (e: Exception) {
                        val firstPoint = routeData.intersections.first()
                        cameraPositionState.position = CameraPosition.Builder()
                            .target(LatLng(firstPoint.latitude, firstPoint.longitude))
                            .zoom(13f)
                            .build()
                        isCameraCentered.value = true
                    }
                    return@LaunchedEffect
                }
            }

            location.value?.let { userLocation ->
                cameraPositionState.position = CameraPosition.Builder()
                    .target(userLocation)
                    .zoom(14f)
                    .build()
                isCameraCentered.value = true
            }
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
        location.value?.let { position ->
            Marker(
                state = MarkerState(position = position),
                title = "Mi ubicación"
            )
        }

        route.value?.let { routeData ->
            val polylinePoints = routeData.intersections.map { intersection ->
                LatLng(intersection.latitude, intersection.longitude)
            }

            if (polylinePoints.isNotEmpty()) {
                Polyline(
                    points = polylinePoints,
                    color = Color(0xFF4285F4),
                    width = 8f,
                    geodesic = true
                )

                if (polylinePoints.size >= 2) {
                    Marker(
                        state = MarkerState(position = polylinePoints.first()),
                        title = "Inicio",
                        snippet = "Punto de partida"
                    )
                    Marker(
                        state = MarkerState(position = polylinePoints.last()),
                        title = "Destino",
                        snippet = "Punto de llegada"
                    )
                }
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