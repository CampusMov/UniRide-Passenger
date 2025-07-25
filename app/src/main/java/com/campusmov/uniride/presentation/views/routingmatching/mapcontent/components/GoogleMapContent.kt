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
import com.campusmov.uniride.domain.shared.model.EUserCarpoolState
import com.campusmov.uniride.presentation.views.routingmatching.mapcontent.MapContentViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
import kotlinx.coroutines.delay
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

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
    val routeCarpool = viewModel.routeCarpool.collectAsState()
    val userCarpoolState = viewModel.userCarpoolState.collectAsState()

    var isCameraCentered = remember {
        mutableStateOf(false)
    }

    // Estate for the carpool marker and its rotation
    val carMarkerState = remember { MarkerState() }
    val carRotation = remember { mutableStateOf(0f) }
    val previousCarPosition = remember { mutableStateOf<LatLng?>(null) }

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

    // Animation for carpool marker position and rotation
    LaunchedEffect(routeCarpool.value) {
        if (userCarpoolState.value == EUserCarpoolState.IN_CARPOOL) {
            routeCarpool.value?.let { carpoolData ->
                val newPosition = LatLng(
                    carpoolData.carpoolCurrentLocation.latitude,
                    carpoolData.carpoolCurrentLocation.longitude
                )

                // Calculate bearing and update rotation
                previousCarPosition.value?.let { prevPos ->
                    val bearing = calculateBearing(prevPos, newPosition)
                    carRotation.value = bearing
                }

                // Animate marker to new position
                animateMarkerToPosition(carMarkerState, newPosition)

                // Update previous position
                previousCarPosition.value = newPosition
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
        // Show user location marker only if not in carpool
        if (userCarpoolState.value != EUserCarpoolState.IN_CARPOOL) {
            location.value?.let { position ->
                Marker(
                    state = MarkerState(position = position),
                    title = "Mi ubicación"
                )
            }
        }

        // Show carpool marker if user is in a carpool
        if (userCarpoolState.value == EUserCarpoolState.IN_CARPOOL) {
            routeCarpool.value?.let { carpoolData ->
                val carPosition = LatLng(
                    carpoolData.carpoolCurrentLocation.latitude,
                    carpoolData.carpoolCurrentLocation.longitude
                )

                // Init car marker position if not set
                if (carMarkerState.position.latitude == 0.0 && carMarkerState.position.longitude == 0.0) {
                    carMarkerState.position = carPosition
                }

                Marker(
                    state = carMarkerState,
                    title = "Carpool",
                    snippet = "Vehículo en ruta",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                    rotation = carRotation.value
                )
            }
        }

        // Show route polyline and markers
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

/*
* Function to animate a marker to a target position smoothly.
* */
suspend fun animateMarkerToPosition(markerState: MarkerState, targetPosition: LatLng) {
    val startPosition = markerState.position
    val animationDuration = 1000L // 1 segundo
    val steps = 60 // 60 FPS
    val stepDuration = animationDuration / steps

    for (i in 0..steps) {
        val progress = i.toFloat() / steps
        val lat = startPosition.latitude + (targetPosition.latitude - startPosition.latitude) * progress
        val lng = startPosition.longitude + (targetPosition.longitude - startPosition.longitude) * progress

        markerState.position = LatLng(lat, lng)
        delay(stepDuration)
    }
}

/*
* Function to calculate the bearing between two LatLng points.
* */
fun calculateBearing(start: LatLng, end: LatLng): Float {
    val lat1 = Math.toRadians(start.latitude)
    val lat2 = Math.toRadians(end.latitude)
    val deltaLng = Math.toRadians(end.longitude - start.longitude)

    val x = sin(deltaLng) * cos(lat2)
    val y = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(deltaLng)

    val bearing = Math.toDegrees(atan2(x, y))
    return ((bearing + 360) % 360).toFloat()
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