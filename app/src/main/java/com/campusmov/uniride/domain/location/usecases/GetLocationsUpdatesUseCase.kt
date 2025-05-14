package com.campusmov.uniride.domain.location.usecases

import com.campusmov.uniride.domain.routingmatching.repository.LocationRepository
import com.google.android.gms.maps.model.LatLng

class GetLocationsUpdatesUseCase(private val repository: LocationRepository) {
    operator fun invoke(callback: (position: LatLng) -> Unit) = repository.getLocationUpdates(callback)
}