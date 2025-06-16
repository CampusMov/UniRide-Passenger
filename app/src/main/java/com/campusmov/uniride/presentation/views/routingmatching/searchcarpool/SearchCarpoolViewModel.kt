package com.campusmov.uniride.presentation.views.routingmatching.searchcarpool

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.domain.routingmatching.model.Carpool
import com.campusmov.uniride.domain.routingmatching.usecases.CarpoolUseCases
import com.campusmov.uniride.domain.shared.model.Location
import com.campusmov.uniride.domain.shared.util.Resource
import com.google.android.libraries.places.api.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchCarpoolViewModel @Inject constructor (
    private val carpoolUseCases: CarpoolUseCases
): ViewModel() {
    private val _amountSeatsRequested = MutableStateFlow<Int>(1)
    val amountSeatsRequested: StateFlow<Int> get() = _amountSeatsRequested

    private val _availableCarpools = MutableStateFlow<List<Carpool>>(emptyList())
    val availableCarpools: StateFlow<List<Carpool>> get() = _availableCarpools

    private val _isLoading  = MutableStateFlow(false)
    val isLoading: MutableStateFlow<Boolean> get() = _isLoading

    fun increaseAmountSeatsRequested() {
        if (_amountSeatsRequested.value < 4) {
            _amountSeatsRequested.value++
        }
    }

    fun decreaseAmountSeatsRequested() {
        if (_amountSeatsRequested.value > 1) {
            _amountSeatsRequested.value--
        }
    }

    fun searchAvailableCarpools(place: Place?, classSchedule: ClassSchedule?, requestedSeats: Int?) {
        if (!isValidSearch(place, classSchedule, requestedSeats)) {
            Log.e("TAG", "Invalid search parameters")
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            val result = carpoolUseCases.searchCarpoolsAvailable(Location.fromPlace(place!!), classSchedule!!, requestedSeats!!)
            when (result) {
                is Resource.Success -> {
                    Log.d("TAG", "successfully searched available carpools: ${result.data}")
                    _availableCarpools.value = result.data
                    _isLoading.value = false
                }
                is Resource.Failure -> {
                    Log.e("TAG", "failed to search available carpools: ${result.message}")
                    _isLoading.value = false
                }
                Resource.Loading -> {}
            }
        }
    }

    fun isValidSearch(place: Place?, classSchedule: ClassSchedule?, requestedSeats: Int?): Boolean {
        return place != null &&
               classSchedule != null &&
               requestedSeats != null &&
               requestedSeats > 0 &&
               requestedSeats <= 4
    }
}