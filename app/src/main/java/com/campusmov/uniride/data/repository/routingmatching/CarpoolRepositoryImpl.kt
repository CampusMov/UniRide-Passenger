package com.campusmov.uniride.data.repository.routingmatching

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.campusmov.uniride.data.datasource.remote.dto.SearchAvailableCarpoolsRequestDto
import com.campusmov.uniride.data.datasource.remote.service.CarpoolService
import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.domain.routingmatching.model.Carpool
import com.campusmov.uniride.domain.routingmatching.repository.CarpoolRepository
import com.campusmov.uniride.domain.shared.model.Location
import com.campusmov.uniride.domain.shared.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class CarpoolRepositoryImpl(
    private val carpoolService: CarpoolService
): CarpoolRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun searchAvailableCarpools(location: Location, classSchedule: ClassSchedule, requestedSeats: Int): Resource<List<Carpool>> = withContext(Dispatchers.IO) {
        try {
            val searchAvailableCarpoolsRequest = SearchAvailableCarpoolsRequestDto.fromDomain(location, classSchedule, requestedSeats)
            val availableCarpoolsResponse = carpoolService.searchAvailableCarpools(searchAvailableCarpoolsRequest)
            if (availableCarpoolsResponse.isEmpty()) {
                Log.d("TAG", "No available carpools found")
                Resource.Failure("No available carpools found")
            } else {
                Log.d("TAG", "Available carpools fetched successfully")
                Resource.Success(availableCarpoolsResponse.map { it.toDomain() })
            }

        } catch (e: IOException) {
            Log.e("TAG", "Network error while fetching available carpools", e)
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("TAG", "Unexpected error while fetching available carpools", e)
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun searchCarpoolById(carpoolId: String): Resource<Carpool> = withContext(Dispatchers.IO) {
        try {
            val response = carpoolService.getCarpoolById(carpoolId)
            if(response.isSuccessful) {
                val carpoolDto = response.body()
                if (carpoolDto != null) {
                    Resource.Success(carpoolDto.toDomain())
                } else {
                    Resource.Failure("Carpool not found")
                }
            } else {
                Resource.Failure("Error fetching carpool: ${response.errorBody()?.string()}")
            }
        } catch (e: IOException) {
            Log.e("TAG", "Network error while fetching carpool by ID", e)
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("TAG", "Unexpected error while fetching carpool by ID", e)
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }
}