package com.campusmov.uniride.presentation.views.analytics.studentRating

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.analytics.usecases.AnalyticsUseCase
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.usecases.UserUseCase
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.usecases.ProfileUseCases
import com.campusmov.uniride.domain.reputation.model.Valoration
import com.campusmov.uniride.domain.reputation.usecases.ReputationIncentivesUseCase
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentRatingMetricsViewModel @Inject constructor(
    private val analyticsUseCase: AnalyticsUseCase,
    private val reputationUseCase: ReputationIncentivesUseCase,
    private val profileUseCase: ProfileUseCases,
    private val userUseCase: UserUseCase
): ViewModel(){

    private val _rating =  MutableStateFlow(0.0)
    val rating : StateFlow<Double> get() = _rating

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _valorationList = MutableStateFlow<List<Valoration>>(emptyList())
    val valorationList: StateFlow<List<Valoration>> get() = _valorationList

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> get() = _profile

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
                    getStudentAverageRating()
                    getUserValorationList()
                }
                is Resource.Failure -> {}
                Resource.Loading -> {}
            }
        }
    }

    fun getStudentAverageRating() {
        viewModelScope.launch {
            try {
                val userId = _user.value?.id
                userId?.let {
                    val result = analyticsUseCase.getStudentRatingMetrics(it)
                    when(result){
                        is Resource.Success -> {
                            _rating.value = result.data.averageRating ?: 0.0
                            Log.d("TAG", "Rating obtenida: ${_rating.value}")
                        }
                        is Resource.Failure -> {
                            Log.e("StudentRatingMetricsVM", "Error obteniendo rating: ${result.message}")
                        }
                        Resource.Loading -> {
                            Log.d("StudentRatingMetricsVM", "Cargando rating...")
                        }
                    }


                }
            } catch (e: Exception) {
                Log.e("StudentRatingMetricsVM", "Error obteniendo rating: ${e.message}")
            }
        }
    }

    fun getUserValorationList(){
        viewModelScope.launch {
            try {
                val userId = _user.value?.id
                userId?.let {
                    val result =reputationUseCase.getValorationsOfUser(it)
                    when(result){
                        is Resource.Success -> {
                            _valorationList.value = result.data ?: emptyList()
                            Log.d("TAG", "Valoraciones obtenidas: ${_valorationList.value}")
                        }
                        is Resource.Failure -> {
                            Log.e("StudentRatingMetricsVM", "Error obteniendo valoraciones: ${result.message}")
                        }
                        Resource.Loading -> {
                            Log.d("StudentRatingMetricsVM", "Cargando valoraciones...")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("StudentRatingMetricsVM", "Error obteniendo valoraciones: ${e.message}")
            }
        }
    }



    suspend fun getProfileById(profileId: String): Profile? {
        return try {
            val result = profileUseCase.getProfileById(profileId)
            when(result){
                is Resource.Success -> {
                    result.data
                }
                is Resource.Failure -> {
                    Log.e("StudentRatingMetricsVM", "Error obteniendo perfil: ${result.message}")
                    null
                }
                Resource.Loading -> {
                    Log.d("StudentRatingMetricsVM", "Cargando perfil...")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("StudentRatingMetricsVM", "Error obteniendo perfil: ${e.message}")
            null
        }
    }




}