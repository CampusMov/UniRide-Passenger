package com.campusmov.uniride.domain.reputation.model

enum class EPenaltyStatus {
    ACTIVE,
    CANCELLED,
    EXPIRED,
    RECEIVED;

    companion object {
        fun fromString(status: String?): EPenaltyStatus {
            return when (status?.trim()?.uppercase()) {
                "ACTIVE" -> ACTIVE
                "CANCELLED" -> CANCELLED
                "EXPIRED" -> EXPIRED
                "RECEIVED" -> RECEIVED
                else -> CANCELLED
            }
        }
    }
}