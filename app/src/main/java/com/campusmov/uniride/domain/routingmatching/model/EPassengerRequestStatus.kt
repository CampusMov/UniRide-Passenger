package com.campusmov.uniride.domain.routingmatching.model

enum class EPassengerRequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    CANCELLED;

    companion object {
        fun fromString(status: String?): EPassengerRequestStatus {
            return when (status?.trim()?.uppercase()) {
                "PENDING" -> PENDING
                "ACCEPTED" -> ACCEPTED
                "REJECTED" -> REJECTED
                "CANCELLED" -> CANCELLED
                else -> PENDING
            }
        }
    }
}