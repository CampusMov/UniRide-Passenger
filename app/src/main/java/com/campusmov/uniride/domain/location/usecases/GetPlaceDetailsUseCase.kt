package com.campusmov.uniride.domain.location.usecases

import com.campusmov.uniride.domain.location.repository.LocationRepository

class GetPlaceDetailsUseCase (private val repository: LocationRepository) {
    suspend operator fun invoke(placeId: String) = repository.getPlaceDetails(placeId)
}