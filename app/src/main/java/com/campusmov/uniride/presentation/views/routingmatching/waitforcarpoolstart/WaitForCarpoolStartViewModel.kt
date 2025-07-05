package com.campusmov.uniride.presentation.views.routingmatching.waitforcarpoolstart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.analytics.usecases.AnalyticsUseCase
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.usecases.UserUseCase
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.usecases.ProfileUseCases
import com.campusmov.uniride.domain.routingmatching.model.Carpool
import com.campusmov.uniride.domain.routingmatching.model.ECarpoolStatus
import com.campusmov.uniride.domain.routingmatching.model.EPassengerRequestStatus
import com.campusmov.uniride.domain.routingmatching.model.PassengerRequest
import com.campusmov.uniride.domain.routingmatching.usecases.CarpoolUseCases
import com.campusmov.uniride.domain.routingmatching.usecases.CarpoolWsUseCases
import com.campusmov.uniride.domain.routingmatching.usecases.PassengerRequestUseCases
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaitForCarpoolStartViewModel @Inject constructor(
    private val carpoolUseCases: CarpoolUseCases,
    private val carpoolWsUseCases: CarpoolWsUseCases,
    private val profileUseCases: ProfileUseCases,
    private val analyticsUseCases: AnalyticsUseCase,
    private val userUseCase: UserUseCase,
    private val passengerRequestUseCases: PassengerRequestUseCases,

    ): ViewModel(){
    private val _currentCarpool = MutableStateFlow<Carpool?>(null)
    val currentCarpool: StateFlow<Carpool?> get() = _currentCarpool

    private val _isLoading  = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private var _driverProfile = MutableStateFlow<Profile?>(null)
    val driverProfile: StateFlow<Profile?> get() = _driverProfile

    private val _driverRating = MutableStateFlow<Double>(0.0)
    val driverRating: StateFlow<Double> = _driverRating

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _passengerRequest = MutableStateFlow<PassengerRequest?>(null)
    val passengerRequest: StateFlow<PassengerRequest?> get() = _passengerRequest

    fun getUserLocally() {
        viewModelScope.launch {
            when (val result = userUseCase.getUserLocallyUseCase()) {
                is Resource.Success -> {
                    _user.value = result.data
                    getPassengerRequestByPassengerId()
                }
                is Resource.Failure -> {}
                Resource.Loading -> {}
            }
        }
    }


    fun getCarpoolById(carpoolId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = carpoolUseCases.getCarpoolById(carpoolId)
            when (result) {
                is Resource.Success -> {
                    Log.d("TAG", "successfully fetched carpool: ${result.data}")
                    _isLoading.value = false
                    _currentCarpool.value = result.data
                    connectToCarpoolWebSocket()
                }
                is Resource.Failure -> {
                    Log.e("TAG", "failed to fetch carpool: ${result.message}")
                    _isLoading.value = false
                }
                Resource.Loading -> {}
            }
        }
    }

    fun getProfileById(profileId: String) {
        viewModelScope.launch {
            try {
                when(val result = profileUseCases.getProfileById(profileId)){
                    is Resource.Success -> {
                        _driverProfile.value = result.data
                    }
                    is Resource.Failure -> {
                        Log.e("StudentRatingMetricsVM", "Error obteniendo perfil: ${result.message}")
                    }
                    Resource.Loading -> {
                        Log.d("StudentRatingMetricsVM", "Cargando perfil...")
                    }
                }
            } catch (e: Exception) {
                Log.e("StudentRatingMetricsVM", "Error obteniendo perfil: ${e.message}")
            }
        }
    }


    fun getStudentAverageRating(profileId: String) {
        viewModelScope.launch {
            try {
                when(val result = analyticsUseCases.getStudentRatingMetrics(profileId)){
                    is Resource.Success -> {
                        val ratingProfile = result.data.averageRating
                        _driverRating.value = ratingProfile
                        Log.d("TAG", "Rating obtenida: $ratingProfile")
                    }
                    is Resource.Failure -> {
                        Log.e("StudentRatingMetricsVM", "Error obteniendo rating: ${result.message}")
                    }
                    Resource.Loading -> {
                        Log.d("StudentRatingMetricsVM", "Cargando rating...")
                    }
                }
            } catch (e: Exception) {
                Log.e("StudentRatingMetricsVM", "Error obteniendo rating: ${e.message}")
            }
        }
    }

    fun getPassengerRequestByPassengerId() {
        if(_user.value == null || _user.value!!.id.isEmpty()) {
            Log.d("TAG", "User not available to fetch passenger request")
            return
        }
        viewModelScope.launch {
            when (val result = passengerRequestUseCases.getAllPassengerRequestsByPassengerId(_user.value!!.id)) {
                is Resource.Success -> {
                    Log.d("TAG", "Passenger request obtained successfully")
                    _passengerRequest.value = result.data.find { it.carpoolId == _currentCarpool.value?.id }
                }
                is Resource.Failure -> {
                    Log.e("TAG", "Error obtaining passenger request: ${result.message}")
                }
                Resource.Loading -> {}
            }
        }
    }

    fun connectToCarpoolWebSocket(){
        viewModelScope.launch {
            var isConnected = false
            try {
                carpoolWsUseCases.connectCarpoolUseCase()
                isConnected = true
                Log.d("TAG", "WaitForCarpoolStartViewModel: Successfully connected to carpool WebSocket")
            } catch (e: Exception) {
                Log.e("TAG", "WaitForCarpoolStartViewModel: Error connecting to carpool WebSocket", e)
            } finally {
                if (isConnected) subscribeToCarpoolUpdates()
            }
        }
    }

    fun subscribeToCarpoolUpdates() {
        viewModelScope.launch {
           carpoolWsUseCases.subscribeCarpoolStatusUpdatesUseCase(currentCarpool.value!!.id)
               .collect { carpool ->
                   val status: String? = carpool.status.name
                   when(ECarpoolStatus.fromString(status)) {
                          ECarpoolStatus.IN_PROGRESS -> {
                            Log.d("TAG", "WaitForCarpoolStartViewModel: Carpool is now in progress: $carpool")
                            _currentCarpool.value = carpool
                              onCleared()
                          }
                          ECarpoolStatus.CANCELLED -> {
                            Log.d("TAG", "WaitForCarpoolStartViewModel: Carpool was cancelled: $carpool")
                            _currentCarpool.value = carpool
                              onCleared()
                          }
                          else -> {
                            Log.d("TAG", "WaitForCarpoolStartViewModel: Carpool status does not affect the current view: $carpool")
                          }
                   }
               }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("TAG", "WaitForCarpoolStartViewModel: ViewModel cleared, unsubscribing from WebSocket")
        viewModelScope.launch { carpoolWsUseCases.disconnectCarpoolUseCase() }
    }
}