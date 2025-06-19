package com.campusmov.uniride.data.repository.analytics

import com.campusmov.uniride.data.datasource.remote.service.AnalyticsService
import com.campusmov.uniride.domain.analytics.model.StudentRatingMetric
import com.campusmov.uniride.domain.analytics.repository.AnalyticsRepository
import com.campusmov.uniride.domain.shared.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnalyticsRepositoryImpl(
    private val analyticsService: AnalyticsService
): AnalyticsRepository {
    override suspend fun getStudentRatingMetrics(userId: String): Resource<StudentRatingMetric> = withContext(Dispatchers.IO) {
        try {
            val response = analyticsService.getStudentRatingMetrics(userId)
            if(response.isSuccessful){
                val studentRatingMetric = response.body()
                if (studentRatingMetric != null) {
                    Resource.Success(studentRatingMetric.toDomain())
                } else {
                    Resource.Failure("No data found for user ID: $userId")
                }
            } else {
                Resource.Failure("Error fetching student rating metrics: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure("An error occurred while fetching student rating metrics: ${e.message ?: "Unknown error"}")
        }

    }

}