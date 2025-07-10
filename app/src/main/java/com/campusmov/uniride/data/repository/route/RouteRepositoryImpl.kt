package com.campusmov.uniride.data.repository.route

import android.util.Log
import com.campusmov.uniride.data.datasource.remote.dto.RouteRequestDto
import com.campusmov.uniride.data.datasource.remote.service.RouteService
import com.campusmov.uniride.domain.route.model.Intersection
import com.campusmov.uniride.domain.route.model.Route
import com.campusmov.uniride.domain.route.repository.RouteRepository
import com.campusmov.uniride.domain.shared.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

class RouteRepositoryImpl(
    private val routeService: RouteService
): RouteRepository {
    override suspend fun getRoute(startLatitude: Double, startLongitude: Double, endLatitude: Double, endLongitude: Double): Resource<Route>  = withContext(Dispatchers.IO) {
        try {
            val routeRequest = RouteRequestDto(startLatitude, startLongitude, endLatitude, endLongitude)
            val routeResponse = routeService.getRoute(routeRequest)
            Log.d("TAG", "Route fetched successfully: $routeResponse")
            Resource.Success(routeResponse.toDomain())
        } catch (e: IOException) {
            Log.e("TAG", "Network error while fetching available carpools", e)
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("TAG", "Unexpected error while fetching available carpools", e)
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }
}
