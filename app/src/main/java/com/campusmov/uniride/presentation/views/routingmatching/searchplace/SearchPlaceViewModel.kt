package com.campusmov.uniride.presentation.views.routingmatching.searchplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.location.model.PlacePrediction
import com.campusmov.uniride.domain.location.usecases.LocationUsesCases
import com.google.android.libraries.places.api.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPlaceViewModel @Inject constructor(
    private val locationUsesCases: LocationUsesCases
): ViewModel() {
    private val _placePredictions = MutableStateFlow<List<PlacePrediction>>(emptyList())
    val placePredictions: StateFlow<List<PlacePrediction>> get() = _placePredictions

    private val _selectedPlace = MutableStateFlow<Place?>(null)
    val selectedPlace: StateFlow<Place?> get() = _selectedPlace

    fun getPlacePredictions(query: String) = viewModelScope.launch {
        _placePredictions.value = locationUsesCases.getPlacePredictions(query)
    }

    fun getPlaceDetails(placeId: String, onPlaceSelected: () -> Unit) = viewModelScope.launch {
        val place = locationUsesCases.getPlaceDetails(placeId)
        _selectedPlace.value = place
        onPlaceSelected()
    }

    fun resetPlacePredictions() {
        _placePredictions.value = emptyList()
    }
}