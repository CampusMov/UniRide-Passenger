package com.campusmov.uniride.domain.profile.model

enum class EGender {
    MALE,
    FEMALE;

    companion object {
        fun fromString(e: String?): EGender {
            if(e.isNullOrBlank()) return EGender.MALE
            return when (e.trim().equals("male", ignoreCase = true)) {
                true -> EGender.MALE
                else -> EGender.FEMALE
            }
        }
    }
}