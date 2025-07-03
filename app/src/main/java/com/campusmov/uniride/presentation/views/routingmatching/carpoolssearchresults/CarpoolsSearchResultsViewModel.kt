package com.campusmov.uniride.presentation.views.routingmatching.carpoolssearchresults

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.analytics.usecases.AnalyticsUseCase
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.usecases.UserUseCase
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.usecases.ProfileUseCases
import com.campusmov.uniride.domain.routingmatching.model.Carpool
import com.campusmov.uniride.domain.routingmatching.model.EPassengerRequestStatus
import com.campusmov.uniride.domain.routingmatching.model.PassengerRequest
import com.campusmov.uniride.domain.routingmatching.usecases.PassengerRequestUseCases
import com.campusmov.uniride.domain.routingmatching.usecases.PassengerRequestWsUseCases
import com.campusmov.uniride.domain.shared.model.Location
import com.campusmov.uniride.domain.shared.util.Resource
import com.google.android.libraries.places.api.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarpoolsSearchResultsViewModel @Inject constructor(
    private val passengerRequestUseCases: PassengerRequestUseCases,
    private val userUseCase: UserUseCase,
    private val profileUseCases: ProfileUseCases,
    private val analyticsUseCases: AnalyticsUseCase,
    private val passengerRequestWsUseCases: PassengerRequestWsUseCases
): ViewModel() {

    private val _profiles = MutableStateFlow<Map<String, Profile?>>(emptyMap())
    val profiles: StateFlow<Map<String, Profile?>> = _profiles

    private val _ratings = MutableStateFlow<Map<String, Double>>(emptyMap())
    val ratings: StateFlow<Map<String, Double>> = _ratings

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _passengerRequests = MutableStateFlow<List<PassengerRequest>>(emptyList())
    val passengerRequests: StateFlow<List<PassengerRequest>> = _passengerRequests

    private val subscriptionJobs = mutableMapOf<String, Job>()

    var passengerRequestAccepted = mutableStateOf<PassengerRequest?>(null)
        private set

    private fun getUserLocally() {
        viewModelScope.launch {
            when (val result = userUseCase.getUserLocallyUseCase()) {
                is Resource.Success -> {
                    _user.value = result.data
                    getPassengerRequestsByPassengerId()
                }
                is Resource.Failure -> {}
                Resource.Loading -> {}
            }
        }
    }

    fun getProfileById(profileId: String) {
        viewModelScope.launch {
            try {
                when(val result = profileUseCases.getProfileById(profileId)){
                    is Resource.Success -> {
                        _profiles.update { it + (profileId to result.data) }
                    }
                    is Resource.Failure -> {
                        Log.e("StudentRatingMetricsVM", "Error obteniendo perfil: ${result.message}")
                        _profiles.update { it + (profileId to null) }
                    }
                    Resource.Loading -> {
                        Log.d("StudentRatingMetricsVM", "Cargando perfil...")
                    }
                }
            } catch (e: Exception) {
                Log.e("StudentRatingMetricsVM", "Error obteniendo perfil: ${e.message}")
                _profiles.update { it + (profileId to null) }
            }
        }
    }

    fun getStudentAverageRating(profileId: String) {
        viewModelScope.launch {
            try {
                when(val result = analyticsUseCases.getStudentRatingMetrics(profileId)){
                    is Resource.Success -> {
                        val ratingProfile = result.data.averageRating
                        _ratings.update { it + (profileId to ratingProfile) }

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

    private fun isValidPassengerRequestToSave(pickUpLocation: Place?, amountSeatsRequested: Int): Boolean {
        return pickUpLocation != null
                && amountSeatsRequested > 0
                && amountSeatsRequested <=4
                && _user.value != null && _user.value!!.id.isNotEmpty()
    }

    fun savePassengerRequest(carpool: Carpool, pickUpLocation: Place?, amountSeatsRequested: Int) {
        if (!isValidPassengerRequestToSave(pickUpLocation, amountSeatsRequested)) {
            Log.d("TAG", "Invalid passenger request parameters")
            return
        }
        val passengerRequestToSave = PassengerRequest(
            passengerId = _user.value!!.id,
            carpoolId = carpool.id,
            pickUpLocation = Location.fromPlace(pickUpLocation!!),
            requestedSeats = amountSeatsRequested
        )
        viewModelScope.launch {
            when (val result = passengerRequestUseCases.savePassengerRequest(passengerRequestToSave)) {
                is Resource.Success -> {
                    Log.d("RegisterProfileVM", "Profile saved")
                    getPassengerRequestsByPassengerId()
                }
                is Resource.Failure -> {
                    Log.e("RegisterProfileVM", "Error saving: ${result.message}")
                }
                Resource.Loading -> {}
            }
        }
    }

    fun getPassengerRequestsByPassengerId() {
        if(_user.value == null || _user.value!!.id.isEmpty()) {
            Log.d("TAG", "User not available to fetch passenger requests")
            return
        }
        viewModelScope.launch {
            when (val result = passengerRequestUseCases.getAllPassengerRequestsByPassengerId(_user.value!!.id)) {
                is Resource.Success -> {
                    Log.d("TAG", "Passenger requests obtained successfully")
                    _passengerRequests.value = result.data
                    val hasAcceptedRequest = _passengerRequests.value.any { it.status == EPassengerRequestStatus.ACCEPTED }
                    if (hasAcceptedRequest) {
                        Log.d("TAG", "Passenger request already accepted, clearing requests")
                        passengerRequestAccepted.value = _passengerRequests.value.first { it.status == EPassengerRequestStatus.ACCEPTED }
                        _passengerRequests.value = emptyList()
                        onCleared()
                    } else {
                        Log.d("TAG", "No accepted passenger requests, subscribing to updates")
                        _passengerRequests.value.forEach { passengerRequest ->
                            subscribeTo(passengerRequest.id)
                        }
                    }
                }
                is Resource.Failure -> {
                    Log.e("TAG", "Error obtaining passenger requests: ${result.message}")
                }
                Resource.Loading -> {}
            }
        }
    }

    fun connectToPassengerRequestWebSocket() {
        viewModelScope.launch {
            try {
                passengerRequestWsUseCases.connectRequestsUseCase()
                Log.d("TAG", "Connected to Passenger Request WebSocket")
            } catch (e: Exception) {
                Log.e("TAG", "Error connecting to Passenger Request WebSocket: ${e.message}")
            }
            getUserLocally()
        }
    }

    fun subscribeTo(requestId: String) {
        if (subscriptionJobs.containsKey(requestId)) return
        val job = viewModelScope.launch {
            passengerRequestWsUseCases.subscribeRequestStatusUpdatesUseCase(requestId)
                .collect { passengerRequest ->
                    val status: String? = passengerRequest.status.name
                    when(EPassengerRequestStatus.fromString(status)) {
                        EPassengerRequestStatus.ACCEPTED -> {
                            Log.d("TAG", "Passenger request accepted: ${passengerRequest.id}")
                            passengerRequestAccepted.value = passengerRequest
                            onCleared()
                            _passengerRequests.value = emptyList()
                        }
                        EPassengerRequestStatus.REJECTED -> {
                            Log.d("TAG", "Passenger request rejected: ${passengerRequest.id}")
                            unsubscribeFrom(requestId)
                            _passengerRequests.update { requests ->
                                requests.filter { it.id != passengerRequest.id }
                            }
                        }
                        else -> {}
                    }
                }
        }
        subscriptionJobs[requestId] = job
    }

    fun unsubscribeFrom(requestId: String) {
        subscriptionJobs.remove(requestId)?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        subscriptionJobs.values.forEach { it.cancel() }
        viewModelScope.launch { passengerRequestWsUseCases.disconnectRequestsUseCase() }
    }
}