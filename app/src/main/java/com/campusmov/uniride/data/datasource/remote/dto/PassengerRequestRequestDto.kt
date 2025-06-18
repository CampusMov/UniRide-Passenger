package com.campusmov.uniride.data.datasource.remote.dto

import com.campusmov.uniride.domain.routingmatching.model.PassengerRequest
import retrofit2.http.Field

data class PassengerRequestRequestDto(
    @Field("carpoolId")
    val carpoolId: String,
    @Field("passengerId")
    val passengerId: String,
    @Field("pickupLocation")
    val pickupLocation: LocationRequestDto,
    @Field("requestedSeats")
    val requestedSeats: Int
) {
    companion object {
        fun fromDomain(passengerRequest: PassengerRequest): PassengerRequestRequestDto {
            return PassengerRequestRequestDto(
                carpoolId = passengerRequest.carpoolId,
                passengerId = passengerRequest.passengerId,
                pickupLocation = LocationRequestDto(
                    name = passengerRequest.pickUpLocation.name,
                    address = passengerRequest.pickUpLocation.address,
                    latitude = passengerRequest.pickUpLocation.latitude,
                    longitude = passengerRequest.pickUpLocation.longitude
                ),
                requestedSeats = passengerRequest.requestedSeats
            )
        }
    }
}