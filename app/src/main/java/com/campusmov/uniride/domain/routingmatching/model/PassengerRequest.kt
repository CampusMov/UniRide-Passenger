package com.campusmov.uniride.domain.routingmatching.model

import com.campusmov.uniride.domain.shared.model.Location

data class PassengerRequest(
    val id: String = "1",
    val passengerId: String = "",
    val carpoolId: String = "",
    val status: EPassengerRequestStatus = EPassengerRequestStatus.PENDING,
    val pickUpLocation: Location = Location(),
    val requestedSeats: Int = 0,
)
