package com.campusmov.uniride.data.datasource.remote.service

import com.campusmov.uniride.data.datasource.remote.dto.StudentRatingMetricResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AnalyticsService {
    @GET("analytics/student-metrics/{userId}")
    suspend fun getStudentRatingMetrics(@Path ("userId") userId: String): Response<StudentRatingMetricResponseDto>
}