package com.campusmov.uniride.presentation.views.routingmatching.mapcontent

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.location.usecases.LocationUsesCases
import com.campusmov.uniride.domain.shared.model.EUserCarpoolState
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
    private val _userCarpoolSate = MutableStateFlow<EUserCarpoolState>(EUserCarpoolState.SEARCHING)
    val userCarpoolState: StateFlow<EUserCarpoolState> get() = _userCarpoolSate

    val carpoolAcceptedId = mutableStateOf<String?>(null)

    fun searchCarpool() {
        _userCarpoolSate.value = EUserCarpoolState.SEARCHING
    }

    fun waitForCarpoolStart() {
        _userCarpoolSate.value = EUserCarpoolState.WAITING_FOR_CARPOOL_START
    }

    fun inCarpool() {
        _userCarpoolSate.value = EUserCarpoolState.IN_CARPOOL
    }

    fun completeCarpool() {
        _userCarpoolSate.value = EUserCarpoolState.COMPLETED
    }

    fun cancelCarpool() {
        _userCarpoolSate.value = EUserCarpoolState.CANCELLED
    }
}