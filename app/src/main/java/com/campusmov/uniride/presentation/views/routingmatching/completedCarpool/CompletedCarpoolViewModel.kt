package com.campusmov.uniride.presentation.views.routingmatching.completedCarpool

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.usecases.ProfileUseCases
import com.campusmov.uniride.domain.routingmatching.model.Carpool
import com.campusmov.uniride.domain.routingmatching.usecases.CarpoolUseCases
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompletedCarpoolViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val carpoolUseCases: CarpoolUseCases
    ): ViewModel() {

    private val _currentCarpool = MutableStateFlow<Carpool?>(null)
    val currentCarpool: StateFlow<Carpool?> get() = _currentCarpool



    private var _driverProfile = MutableStateFlow<Profile?>(null)
    val driverProfile: StateFlow<Profile?> get() = _driverProfile

    fun getCarpoolById(carpoolId: String) {
        viewModelScope.launch {
            val result = carpoolUseCases.getCarpoolById(carpoolId)
            when (result) {
                is Resource.Success -> {
                    Log.d("TAG", "successfully fetched carpool: ${result.data}")
                    _currentCarpool.value = result.data
                }
                is Resource.Failure -> {
                    Log.e("TAG", "failed to fetch carpool: ${result.message}")
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

//    fun sendValoration(driverId: String, userId: String ,rating: Int, message: String){
//        viewModelScope.launch {
//
//        }
//    }


}