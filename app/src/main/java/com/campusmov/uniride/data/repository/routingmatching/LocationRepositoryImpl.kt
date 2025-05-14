package com.campusmov.uniride.data.repository.routingmatching

import com.campusmov.uniride.data.datasource.location.LocationDataSource
import com.campusmov.uniride.domain.routingmatching.repository.LocationRepository
import com.google.android.gms.maps.model.LatLng

class LocationRepositoryImpl(private val locationDataSource: LocationDataSource): LocationRepository {
    override fun getLocationUpdates(callback: (LatLng) -> Unit) {
        locationDataSource.getLocationUpdates(callback)
    }
}