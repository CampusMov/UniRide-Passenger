package com.campusmov.uniride.domain.analytics.model

/**
 * Representa una métrica de calificación asociada a un estudiante dentro del dominio analítico de UniRide.
 *
 * Esta clase de datos consolida las estadísticas de reputación de un usuario,
 * incluyendo la cantidad total de calificaciones recibidas, el número de reseñas
 * y el promedio general obtenido.
 *
 * @property userId Identificador único del estudiante al que pertenecen las métricas.
 * @property totalRatings Suma total de las calificaciones numéricas recibidas.
 * @property totalReviewsCount Número total de reseñas registradas para el estudiante.
 * @property averageRating Promedio general de calificación obtenido a partir de las reseñas.
 */

data class StudentRatingMetric (
    val userId: String,
    val totalRatings: Double,
    val totalReviewsCount: Int,
    val averageRating: Double
)
