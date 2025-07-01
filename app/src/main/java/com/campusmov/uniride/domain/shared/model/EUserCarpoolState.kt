package com.campusmov.uniride.domain.shared.model

enum class EUserCarpoolState {
    SEARCHING,
    WAITING_FOR_CARPOOL_START,
    IN_CARPOOL,
    COMPLETED,
    CANCELLED;

    companion object {
        fun fromString(state: String?): EUserCarpoolState {
            return when (state?.trim()?.uppercase()) {
                "SEARCHING" -> SEARCHING
                "WAITING_FOR_CARPOOL_START" -> WAITING_FOR_CARPOOL_START
                "IN_CARPOOL" -> IN_CARPOOL
                "COMPLETED" -> COMPLETED
                "CANCELLED" -> CANCELLED
                else -> SEARCHING
            }
        }
    }
}