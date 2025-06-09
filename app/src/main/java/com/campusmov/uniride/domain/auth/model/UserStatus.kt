package com.campusmov.uniride.domain.auth.model

enum class UserStatus {
    NOT_VERIFIED,
    VERIFIED,
    ACTIVE,
    BLOCKED,
    DELETED;

    companion object {
        fun fromString(value: String): UserStatus {
            return when (value.lowercase()) {
                "not_verified" -> NOT_VERIFIED
                "verified" -> VERIFIED
                "active" -> ACTIVE
                "blocked" -> BLOCKED
                "deleted" -> DELETED
                else -> NOT_VERIFIED
            }
        }
    }
}