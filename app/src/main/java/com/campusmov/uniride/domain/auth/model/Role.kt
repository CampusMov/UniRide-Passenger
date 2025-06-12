package com.campusmov.uniride.domain.auth.model

enum class Role {
    DRIVER,
    PASSENGER;

    companion object {
        fun fromString(role: String): Role {
            return when (role.lowercase()) {
                "driver" -> DRIVER
                "passenger" -> PASSENGER
                else -> throw IllegalArgumentException("Unknown role: $role")
            }
        }
    }
}