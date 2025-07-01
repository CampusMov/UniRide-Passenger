package com.campusmov.uniride.presentation.views.routingmatching.menunavigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.analytics.usecases.AnalyticsUseCase
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.usecases.UserUseCase
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.usecases.ProfileUseCases
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuNavigationViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val userUseCase: UserUseCase,
    private val analyticsUseCase: AnalyticsUseCase
    ): ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> get() = _profile

    private val _rating =  MutableStateFlow(0.0)
    val rating : StateFlow<Double> get() = _rating

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
                    getProfileById()
                    getStudentAverageRating()
                }
                is Resource.Failure -> {
                    Log.e("MenuNavigationVM", "Error obteniendo usuario: ${result.message}")
                }
                Resource.Loading -> {
                    Log.d("MenuNavigationVM", "Cargando usuario...")
                }
            }
        }
    }

    fun getProfileById() {
        viewModelScope.launch {
            try {
                val result = profileUseCases.getProfileById(_user.value?.id ?: "")
                when(result){
                    is Resource.Success -> {
                        _profile.value = result.data
                    }
                    is Resource.Failure -> {
                        Log.e("MenuNavigationVM", "Error obteniendo perfil: ${result.message}")
                    }
                    Resource.Loading -> {
                        Log.d("MenuNavigationVM", "Cargando perfil...")
                    }
                }
            } catch (e: Exception) {
                Log.e("MenuNavigationVM", "Error obteniendo perfil: ${e.message}")
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
                            Log.e("MenuNavigationVM", "Error obteniendo rating: ${result.message}")
                        }
                        Resource.Loading -> {
                            Log.d("MenuNavigationVM", "Cargando rating...")
                        }
                    }


                }
            } catch (e: Exception) {
                Log.e("MenuNavigationVM", "Error obteniendo rating: ${e.message}")
            }
        }
    }



}