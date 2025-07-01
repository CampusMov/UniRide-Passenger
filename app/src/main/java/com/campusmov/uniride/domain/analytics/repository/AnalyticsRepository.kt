package com.campusmov.uniride.domain.analytics.repository

import com.campusmov.uniride.domain.analytics.model.StudentRatingMetric
import com.campusmov.uniride.domain.shared.util.Resource

interface AnalyticsRepository {
    suspend fun getStudentRatingMetrics(userId: String): Resource<StudentRatingMetric>
}