package com.campusmov.uniride.domain.reputation.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Infraction(
    val id: String,
    val userId: String,
    val type: String,
    val status: EPenaltyStatus,
    val description: String,
    val timestamp: Date
) {
    fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        return sdf.format(date)
    }
}