package com.campusmov.uniride.domain.shared.model

import com.google.android.libraries.places.api.model.Place

data class Location (
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "",
    val name: String = ""
) {
    companion object {
        fun fromPlace(place: Place): Location {
            return Location(
                latitude = place.location?.latitude?: 0.0,
                longitude = place.location?.longitude ?: 0.0,
                address = place.formattedAddress?: "",
                name = place.displayName ?: ""
            )
        }
    }
}