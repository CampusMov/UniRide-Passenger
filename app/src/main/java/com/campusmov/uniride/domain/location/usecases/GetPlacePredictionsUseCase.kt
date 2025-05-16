package com.campusmov.uniride.domain.location.usecases

import com.campusmov.uniride.domain.location.repository.LocationRepository

class GetPlacePredictionsUseCase(private val repository: LocationRepository) {
    suspend operator fun invoke(query: String) = repository.getPlacePredictions(query)
}