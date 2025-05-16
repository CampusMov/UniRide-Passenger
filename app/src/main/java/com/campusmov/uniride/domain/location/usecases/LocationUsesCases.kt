package com.campusmov.uniride.domain.location.usecases

data class LocationUsesCases(
    val getLocationUpdates: GetLocationsUpdatesUseCase,
    val getPlacePredictions: GetPlacePredictionsUseCase,
    val getPlaceDetails: GetPlaceDetailsUseCase,
)
