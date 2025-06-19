package com.campusmov.uniride.data.repository.routingmatching

import android.util.Log
import com.campusmov.uniride.data.datasource.remote.mapper.toRequestBody
import com.campusmov.uniride.data.datasource.remote.service.PassengerRequestService
import com.campusmov.uniride.domain.routingmatching.model.PassengerRequest
import com.campusmov.uniride.domain.routingmatching.repository.PassengerRequestRepository
import com.campusmov.uniride.domain.shared.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class PassengerRequestRepositoryImpl(
    private val passengerRequestService: PassengerRequestService
): PassengerRequestRepository {
    override suspend fun savePassengerRequest(passengerRequest: PassengerRequest): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            val passengerRequestRequest = passengerRequest.toRequestBody()
            passengerRequestService.savePassengerRequest(passengerRequestRequest)
            Log.d("TAG", "Passenger request created successfully")
            Resource.Success(Unit)
        } catch (e: IOException) {
            Log.e("TAG", "Network error while saving passenger request", e)
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("TAG", "Unexpected error while saving passenger request", e)
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun getAllPassengerRequestsByPassengerId(passengerId: String): Resource<List<PassengerRequest>> = withContext(Dispatchers.IO) {
        try {
            val passengerRequestRequestList = passengerRequestService.getAllPassengerRequestsByPassengerId(passengerId)
            Log.d("TAG", "Passenger requests fetched successfully for passenger ID: $passengerId")
            Resource.Success(passengerRequestRequestList.map { it.toDomain() })
        } catch (e: IOException) {
            Log.e("TAG", "Network error while saving passenger request", e)
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("TAG", "Unexpected error while saving passenger request", e)
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }
}