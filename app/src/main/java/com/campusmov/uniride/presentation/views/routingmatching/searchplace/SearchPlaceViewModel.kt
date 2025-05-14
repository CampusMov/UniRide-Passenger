package com.campusmov.uniride.presentation.views.routingmatching.searchplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.location.model.PlacePrediction
import com.google.android.libraries.places.api.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPlaceViewModel @Inject constructor(
    // TODO: Implement the LocationUseCases
    // TODO: ClientRequestUseCases
): ViewModel() {
    private val _placePredictions = MutableStateFlow<List<PlacePrediction>>(listOf(
        PlacePrediction(
            "1",
            "Av. Alberto del Campo 123, San Isidro 15073, Lima, Peru",
        ),
        PlacePrediction(
            "2",
            "Av. José Larco 812, Miraflores 15074, Lima, Peru",
        ),
        PlacePrediction(
            "3",
            "Av. Brasil 123, Breña 15063, Lima, Peru",
        ),
        PlacePrediction(
            "4",
            "Av. Javier Prado Este 123, San Isidro 15073, Lima, Peru",
        ),
        PlacePrediction(
            "5",
            "Av. José de la Riva Agüero 123, San Isidro 15073, Lima, Peru",
        ),
    ))
    val placePredictions: StateFlow<List<PlacePrediction>> = _placePredictions

    fun getPlacePredictions(query: String) = viewModelScope.launch {
        // TODO: Implement the logic to get the place predictions
    }

    fun getPlaceDetails(placeId: String, onPlaceSelected: (place: Place) -> Unit) = viewModelScope.launch {
        // TODO: Implement locationUseCases
        val place = Place.builder()
            .setId(placeId)
            .build()
        onPlaceSelected(place)
    }
}