package com.campusmov.uniride.presentation.views.routingmatching.mapcarpoolsearcher

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapCarpoolSearcherView(
    viewModel: MapCarpoolSearcherViewModel = hiltViewModel(),
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
        if (hasPermission.value) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                cameraPositionState = cameraPositionState,
                properties = mapProperties.value
            ) {
                location.let { position ->
                    if (position.value != null) {
                        Marker(
                            state = MarkerState(position = position.value!!),
                        )
                    }
                }
            }
        } else {
            Text(
                text = "Location permission is required to use this feature.",
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}