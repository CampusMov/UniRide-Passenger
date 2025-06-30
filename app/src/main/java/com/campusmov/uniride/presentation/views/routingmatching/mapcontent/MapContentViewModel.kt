package com.campusmov.uniride.presentation.views.routingmatching.mapcontent

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.location.usecases.LocationUsesCases
import com.campusmov.uniride.domain.route.model.Route
import com.campusmov.uniride.domain.route.usecases.RouteUseCases
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapContentViewModel @Inject constructor(
    private val locationUsesCases: LocationUsesCases,
    private val routeUseCase: RouteUseCases
): ViewModel() {
    private val _location = MutableStateFlow<LatLng?>(null)
    val location: StateFlow<LatLng?> get() = _location

    private val _route = MutableStateFlow<Route?>(null)
    val route: StateFlow<Route?> get() = _route

    var isInteractiveWithMap = mutableStateOf(false)

    fun startLocationUpdates() = viewModelScope.launch {
        locationUsesCases.getLocationUpdates { position ->
            _location.value = position
        }
    }

    fun loadRoute() = viewModelScope.launch {
        try {
            val routeData = routeUseCase.getRoute()
            _route.value = routeData
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}