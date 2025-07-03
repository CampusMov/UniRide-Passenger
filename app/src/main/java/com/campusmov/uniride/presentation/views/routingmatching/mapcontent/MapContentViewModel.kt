package com.campusmov.uniride.presentation.views.routingmatching.mapcontent

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.usecases.UserUseCase
import com.campusmov.uniride.domain.location.usecases.LocationUsesCases
import com.campusmov.uniride.domain.route.model.Route
import com.campusmov.uniride.domain.route.usecases.RouteUseCases
import com.campusmov.uniride.domain.shared.model.EUserCarpoolState
import com.campusmov.uniride.domain.shared.util.Resource
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapContentViewModel @Inject constructor(
    private val locationUsesCases: LocationUsesCases,
    private val routeUseCase: RouteUseCases,
    private val userUseCase: UserUseCase
): ViewModel() {
    private val _location = MutableStateFlow<LatLng?>(null)
    val location: StateFlow<LatLng?> get() = _location

    private val _route = MutableStateFlow<Route?>(null)
    val route: StateFlow<Route?> get() = _route

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    var isInteractiveWithMap = mutableStateOf(false)

    var showSearchPickUpPoint = mutableStateOf(false)
    var showSearchClassSchedule = mutableStateOf(false)
    var showCarpoolsSearchResults = mutableStateOf(false)

    var showChat = mutableStateOf(false)

    private val _userCarpoolSate = MutableStateFlow<EUserCarpoolState>(EUserCarpoolState.SEARCHING)
    val userCarpoolState: StateFlow<EUserCarpoolState> get() = _userCarpoolSate

    val carpoolAcceptedId = mutableStateOf<String?>(null)

    init {
        getUserLocally()
    }

    private fun getUserLocally() {
        viewModelScope.launch {
            val result = userUseCase.getUserLocallyUseCase()
            when (result) {
                is Resource.Success -> {
                    _user.value = result.data
                    Log.d("TAG", "getUser: ${result.data}")
                }
                is Resource.Failure -> {
                    Log.e("TAG", "Failed to get user: ${result.message}")
                }
                Resource.Loading -> {}
            }
        }
    }

    fun startLocationUpdates() = viewModelScope.launch {
        locationUsesCases.getLocationUpdates { position ->
            _location.value = position
        }
    }

    fun loadRoute(startLatitude: Double, startLongitude: Double, endLatitude: Double, endLongitude: Double) {
        if (!isValidRoute(startLatitude, startLongitude, endLatitude, endLongitude)) {
            _route.value = null
            return
        }
        viewModelScope.launch {
           val result = routeUseCase.getRoute(
                startLatitude = startLatitude,
                startLongitude = startLongitude,
                endLatitude = endLatitude,
                endLongitude = endLongitude
           )
            when (result) {
                is Resource.Success -> {
                    Log.d("TAG", "Route fetched successfully: ${result.data}")
                    _route.value = result.data
                }
                is Resource.Failure -> {
                    Log.e("TAG", "Failed to fetch route: ${result.message}")
                    _route.value = null
                }
                Resource.Loading -> {}
            }
        }
    }

    private fun isValidRoute(startLatitude: Double, startLongitude: Double, endLatitude: Double, endLongitude: Double): Boolean {
        return startLatitude != 0.0 && startLongitude != 0.0 &&
                endLatitude != 0.0 && endLongitude != 0.0
    }

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