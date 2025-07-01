package com.campusmov.uniride.data.datasource.remote.dto

import com.campusmov.uniride.domain.routingmatching.model.EPassengerRequestStatus
import com.campusmov.uniride.domain.routingmatching.model.PassengerRequest
import com.campusmov.uniride.domain.shared.model.Location

data class PassengerRequestResponsePayload(
    val id: String?,
    val carpoolId: String?,
    val passengerId: String?,
    val pickupLocationName: String?,
    val pickupLocationAddress: String?,
    val status: String?,
    val requestedSeats: Int?
) {
    fun toDomain(): PassengerRequest {
        return PassengerRequest(
            id = id.orEmpty(),
            carpoolId = carpoolId.orEmpty(),
            passengerId = passengerId.orEmpty(),
            pickUpLocation = Location(
                name = pickupLocationName.orEmpty(),
                address = pickupLocationAddress.orEmpty()
            ),
            status = EPassengerRequestStatus.fromString(status.orEmpty()),
            requestedSeats = requestedSeats ?: 0
        )
    }
}