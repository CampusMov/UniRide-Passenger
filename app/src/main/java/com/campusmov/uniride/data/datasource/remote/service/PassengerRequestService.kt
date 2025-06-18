package com.campusmov.uniride.data.datasource.remote.service

import com.campusmov.uniride.data.datasource.remote.dto.PassengerRequestRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface PassengerRequestService {
    @POST("/passenger-requests")
    suspend fun savePassengerRequest(
        @Body passengerRequestRequestDto: PassengerRequestRequestDto
    )
}