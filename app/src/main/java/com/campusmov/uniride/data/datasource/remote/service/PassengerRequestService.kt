package com.campusmov.uniride.data.datasource.remote.service

import com.campusmov.uniride.data.datasource.remote.dto.PassengerRequestRequestDto
import com.campusmov.uniride.data.datasource.remote.dto.PassengerRequestResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PassengerRequestService {
    @POST("/passenger-requests")
    suspend fun savePassengerRequest(
        @Body passengerRequestRequestDto: PassengerRequestRequestDto
    )

    @GET("/passenger-requests")
    suspend fun getAllPassengerRequestsByPassengerId(
        @Query("passengerId") passengerId: String
    ): List<PassengerRequestResponseDto>
}