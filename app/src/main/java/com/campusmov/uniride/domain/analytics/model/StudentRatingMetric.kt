package com.campusmov.uniride.domain.analytics.model

data class StudentRatingMetric (
    val userId: String,
    val totalRatings: Double,
    val totalReviewsCount: Int,
    val averageRating: Double
)