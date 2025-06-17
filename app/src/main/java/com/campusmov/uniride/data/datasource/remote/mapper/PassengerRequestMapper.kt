package com.campusmov.uniride.data.datasource.remote.mapper

import com.campusmov.uniride.data.datasource.remote.dto.PassengerRequestRequestDto
import com.campusmov.uniride.domain.routingmatching.model.PassengerRequest

fun PassengerRequest.toRequestBody(): PassengerRequestRequestDto =
    PassengerRequestRequestDto(
        carpoolId = carpoolId,
        passengerId = passengerId,
        pickupLocation = pickUpLocation.toRequestBody(),
        requestedSeats = requestedSeats
    )