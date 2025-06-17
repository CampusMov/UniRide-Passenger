package com.campusmov.uniride.domain.analytics.model

data class StudentRatingMetric (
    val userId: String,
    val totalRatings: Int,
    val totalReviewsCount: Double,
    val averageRating: Double
)