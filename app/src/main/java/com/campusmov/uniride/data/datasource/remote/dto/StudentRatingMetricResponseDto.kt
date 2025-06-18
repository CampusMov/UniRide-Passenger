package com.campusmov.uniride.data.datasource.remote.dto

import com.campusmov.uniride.domain.analytics.model.StudentRatingMetric

data class StudentRatingMetricResponseDto (

    val userId: String?,
    val totalRatings: Int?,
    val totalReviewsCount: Double?,
    val averageRating: Double?
){
    fun toDomain(): StudentRatingMetric {
        return StudentRatingMetric(
            userId = userId ?: "",
            totalRatings = totalRatings ?: 0,
            totalReviewsCount = totalReviewsCount ?: 0.0,
            averageRating = averageRating ?: 0.0
        )
    }
}
