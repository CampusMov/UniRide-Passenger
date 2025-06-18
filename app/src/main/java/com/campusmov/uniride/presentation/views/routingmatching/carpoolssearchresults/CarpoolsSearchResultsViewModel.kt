package com.campusmov.uniride.presentation.views.routingmatching.carpoolssearchresults

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.analytics.usecases.AnalyticsUseCase
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.usecases.ProfileUseCases
import com.campusmov.uniride.domain.routingmatching.usecases.CarpoolUseCases
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarpoolsSearchResultsViewModel @Inject constructor(
    private val carpoolUseCases: CarpoolUseCases,
    private val profileUseCases: ProfileUseCases,
    private val analyticsUseCases: AnalyticsUseCase
): ViewModel() {

    private val _profiles = MutableStateFlow<Map<String, Profile?>>(emptyMap())
    val profiles: StateFlow<Map<String, Profile?>> = _profiles

    private val _ratings = MutableStateFlow<Map<String, Double>>(emptyMap())
    val ratings: StateFlow<Map<String, Double>> = _ratings

    fun getProfileById(profileId: String) {
        viewModelScope.launch {
            try {
                val result = profileUseCases.getProfileById(profileId)
                when(result){
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
                val result = analyticsUseCases.getStudentRatingMetrics(profileId)
                when(result){
                    is Resource.Success -> {
                        val ratingObtenido = result.data.averageRating ?: 0.0
                        _ratings.update { it + (profileId to ratingObtenido) }

                        Log.d("TAG", "Rating obtenida: $ratingObtenido")
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




}