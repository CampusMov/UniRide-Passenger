package com.campusmov.uniride.domain.analytics.usecases

import com.campusmov.uniride.domain.analytics.repository.AnalyticsRepository

class StudentRatingUseCase(private val repository: AnalyticsRepository) {
    suspend operator fun invoke(userId: String) = repository.getStudentRatingMetrics(userId)
}