package com.campusmov.uniride.data.datasource.remote.dto

import com.campusmov.uniride.domain.reputation.model.Valoration
import java.util.Date

data class ValorationResponseDto(
    val id: String?,
    val userId: String?,
    val senderId: String?,
    val reputationScore: Double?,
    val message: String?,
    val timestamp: Date?
) {
    fun toDomain(): Valoration {
        return Valoration(
            id = id ?: "",
            userId = userId ?: "",
            senderId = senderId ?: "",
            reputationScore = reputationScore ?: 0.0,
            message = message ?: "",
            timestamp = timestamp ?: Date()
        )
    }
}