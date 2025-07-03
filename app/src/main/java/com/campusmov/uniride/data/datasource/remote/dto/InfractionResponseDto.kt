package com.campusmov.uniride.data.datasource.remote.dto

import com.campusmov.uniride.domain.reputation.model.EPenaltyStatus
import com.campusmov.uniride.domain.reputation.model.Infraction
import java.util.Date

data class InfractionResponseDto(
    val id: String?,
    val userId: String?,
    val type: String?,
    val status: String?,
    val description: String?,
    val timestamp: Date?
) {
    fun toDomain(): Infraction {
        return Infraction(
            id = id ?: "",
            userId = userId ?: "",
            type = type ?: "",
            status = EPenaltyStatus.fromString(status),
            description = description ?: "",
            timestamp = timestamp ?: Date()
        )
    }
}