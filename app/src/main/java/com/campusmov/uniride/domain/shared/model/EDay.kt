package com.campusmov.uniride.domain.shared.model

enum class EDay {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    companion object {
        fun fromString(day: String?): EDay {
            return when (day?.trim()?.uppercase()) {
                "MONDAY" -> MONDAY
                "TUESDAY" -> TUESDAY
                "WEDNESDAY" -> WEDNESDAY
                "THURSDAY" -> THURSDAY
                "FRIDAY" -> FRIDAY
                "SATURDAY" -> SATURDAY
                "SUNDAY" -> SUNDAY
                else -> MONDAY
            }
        }
    }
    fun showDay(): String {
        return when (this) {
            MONDAY -> "Lunes"
            TUESDAY -> "Martes"
            WEDNESDAY -> "Miercoles"
            THURSDAY -> "Jueves"
            FRIDAY -> "Viernes"
            SATURDAY -> "Jueves"
            SUNDAY -> "Domingo"
        }
    }
}