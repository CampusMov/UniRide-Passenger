package com.campusmov.uniride.presentation.views.routingmatching.completedCarpool

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.usecases.UserUseCase
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.usecases.ProfileUseCases
import com.campusmov.uniride.domain.reputation.usecases.ReputationIncentivesUseCase
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
    private val carpoolUseCases: CarpoolUseCases,
    private val reputationUseCases: ReputationIncentivesUseCase,
    private val userUseCase: UserUseCase,
    ): ViewModel() {

    private val _currentCarpool = MutableStateFlow<Carpool?>(null)
    val currentCarpool: StateFlow<Carpool?> get() = _currentCarpool



    private var _driverProfile = MutableStateFlow<Profile?>(null)
    val driverProfile: StateFlow<Profile?> get() = _driverProfile

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    var carpoolFinished = mutableStateOf<Boolean?>(false)
        private set

    fun getCarpoolById(carpoolId: String) {
        viewModelScope.launch {
            Log.d("TAG", "${carpoolFinished.value}")

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
            Log.d("TAG", "${carpoolFinished.value}")
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

    fun sendValoration(driverId: String, userId: String ,rating: Int, message: String){
        viewModelScope.launch {
            try {
                val result = reputationUseCases.createValoration(driverId, userId, rating, message)
                when (result) {
                    is Resource.Success -> {
                        Log.d("TAG", "Valoration sent successfully")
                        carpoolFinished.value = true
                    }
                    is Resource.Failure -> {
                        Log.e("TAG", "Failed to send valoration: ${result.message}")
                    }
                    Resource.Loading -> {
                        Log.d("TAG", "Sending valoration...")
                    }
                }
            } catch (e: Exception) {
                Log.e("TAG", "Error sending valoration: ${e.message}")
            }

        }
    }

    fun getUserLocally() {
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


}