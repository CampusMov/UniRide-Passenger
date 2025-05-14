package com.campusmov.uniride.domain.routingmatching.repository

import com.google.android.gms.maps.model.LatLng

interface LocationRepository {
    fun getLocationUpdates(callback: (position: LatLng) -> Unit)
}