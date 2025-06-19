package com.campusmov.uniride.presentation.views.routingmatching.carpoolssearchresults

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.analytics.usecases.AnalyticsUseCase
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.usecases.UserUseCase
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.usecases.ProfileUseCases
import com.campusmov.uniride.domain.routingmatching.model.Carpool
import com.campusmov.uniride.domain.routingmatching.model.PassengerRequest
import com.campusmov.uniride.domain.routingmatching.usecases.PassengerRequestUseCases
import com.campusmov.uniride.domain.shared.model.Location
import com.campusmov.uniride.domain.shared.util.Resource
import com.google.android.libraries.places.api.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val analyticsUseCases: AnalyticsUseCase
): ViewModel() {

    private val _profiles = MutableStateFlow<Map<String, Profile?>>(emptyMap())
    val profiles: StateFlow<Map<String, Profile?>> = _profiles

    private val _ratings = MutableStateFlow<Map<String, Double>>(emptyMap())
    val ratings: StateFlow<Map<String, Double>> = _ratings

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    init {
        getUserLocally()
    }

    private fun getUserLocally() {
        viewModelScope.launch {
            when (val result = userUseCase.getUserLocallyUseCase()) {
                is Resource.Success -> {
                    _user.value = result.data
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
                }
                is Resource.Failure -> {
                    Log.e("RegisterProfileVM", "Error saving: ${result.message}")
                }
                Resource.Loading -> {}
            }
        }
    }
}