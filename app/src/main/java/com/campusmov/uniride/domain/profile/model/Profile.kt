package com.campusmov.uniride.domain.profile.model

import java.time.LocalDate

data class Profile(
    val userId: String = "1",
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String,
    val birthDate: String?,
    val gender: EGender = EGender.MALE,
    val institutionalEmailAddress: String = "u202500000@upc.edu.pe",
    val personalEmailAddress: String,
    val countryCode: String = "+51",
    val phoneNumber: String,
    val university: String,
    val faculty: String,
    val academicProgram: String,
    val semester: String,
    val classSchedules: List<ClassSchedule>
)
