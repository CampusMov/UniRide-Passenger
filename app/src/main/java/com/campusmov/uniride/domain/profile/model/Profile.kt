package com.campusmov.uniride.domain.profile.model

data class Profile(
    val userId: String,
    val institutionalEmailAddress: String,
    val personalEmailAddress: String,
    val countryCode: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val gender: String,
    val profilePictureUrl: String,
    val university: String,
    val faculty: String,
    val academicProgram: String,
    val semester: String,
    val classSchedules: List<ClassSchedule>
)
