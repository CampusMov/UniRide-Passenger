package com.campusmov.uniride.presentation.views.routingmatching.mapcontent

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.location.usecases.LocationUsesCases
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapContentViewModel @Inject constructor(
    private val locationUsesCases: LocationUsesCases
): ViewModel() {
    private val _location = MutableStateFlow<LatLng?>(null)
    val location: StateFlow<LatLng?> get() = _location

    var isInteractiveWithMap = mutableStateOf(false)

    fun startLocationUpdates() = viewModelScope.launch {
        locationUsesCases.getLocationUpdates { position ->
            _location.value = position
        }
    }
}