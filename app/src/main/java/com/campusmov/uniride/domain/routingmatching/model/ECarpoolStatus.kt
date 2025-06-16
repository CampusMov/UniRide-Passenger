package com.campusmov.uniride.domain.routingmatching.model

enum class ECarpoolStatus {
    CREATED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    companion object {
        fun fromString(status: String?): ECarpoolStatus {
            return when (status?.trim()?.uppercase()) {
                "CREATED" -> CREATED
                "IN_PROGRESS" -> IN_PROGRESS
                "COMPLETED" -> COMPLETED
                "CANCELLED" -> CANCELLED
                else -> CREATED
            }
        }
    }
}