package com.campusmov.uniride.data.datasource.remote.dto

import com.campusmov.uniride.domain.routingmatching.model.EPassengerRequestStatus
import com.campusmov.uniride.domain.routingmatching.model.PassengerRequest
import com.campusmov.uniride.domain.shared.model.Location
import retrofit2.http.Field

data class PassengerRequestResponseDto(
    @Field("id")
    val id: String?,
    @Field("carpoolId")
    val carpoolId: String?,
    @Field("passengerId")
    val passengerId: String?,
    @Field("pickupLocationName")
    val pickupLocationName: String?,
    @Field("pickupLocationAddress")
    val pickupLocationAddress: String?,
    @Field("status")
    val status: String?,
    @Field("requestedSeats")
    val requestedSeats: Int?
) {
    fun toDomain(): PassengerRequest {
        return PassengerRequest(
            id = id ?: "",
            carpoolId = carpoolId ?: "",
            passengerId = passengerId ?: "",
            pickUpLocation = Location(
                address = pickupLocationAddress?: "",
                name = pickupLocationName ?: "",
            ),
            status = EPassengerRequestStatus.fromString(status),
            requestedSeats = requestedSeats ?: 0
        )
    }
}
