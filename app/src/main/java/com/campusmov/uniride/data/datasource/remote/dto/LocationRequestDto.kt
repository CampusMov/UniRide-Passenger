package com.campusmov.uniride.data.datasource.remote.dto

import com.campusmov.uniride.domain.shared.model.Location
import retrofit2.http.Field

data class LocationRequestDto(
    @Field("name")
    val name: String,
    @Field("address")
    val address: String,
    @Field("longitude")
    val longitude: Double,
    @Field("latitude")
    val latitude: Double
) {
    companion object {
        fun fromDomain(location: Location): LocationRequestDto {
            return LocationRequestDto(
                name = location.name,
                address = location.address,
                longitude = location.longitude,
                latitude = location.latitude
            )
        }
    }
}
