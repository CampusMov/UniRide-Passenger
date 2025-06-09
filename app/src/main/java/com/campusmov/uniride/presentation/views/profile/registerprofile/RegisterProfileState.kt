package com.campusmov.uniride.presentation.views.profile.registerprofile

import com.campusmov.uniride.domain.profile.model.EGender
import com.campusmov.uniride.domain.profile.model.Profile
import java.time.LocalDate

data class RegisterProfileState(
    val userId: String = "1",
    val firstName: String = "",
    val lastName: String = "",
    val profilePictureUrl : String = "https://img.freepik.com/vector-gratis/circulo-azul-usuario-blanco_78370-4707.jpg?semt=ais_items_boosted&w=740",
    val birthDate: LocalDate? = null,
    val gender: EGender = EGender.MALE,
    val institutionalEmailAddress: String = "",
    val personalEmailAddress: String = "",
    val countryCode: String = "+51",
    val phoneNumber: String = "",
    val university: String = "",
    val faculty: String = "",
    val academicProgram: String = "",
    val semester: String = ""
) {
    fun toDomain(): Profile {
        return Profile(
            userId = userId,
            firstName = firstName,
            lastName = lastName,
            profilePictureUrl = profilePictureUrl,
            birthDate = birthDate,
            gender = gender,
            institutionalEmailAddress = institutionalEmailAddress,
            personalEmailAddress = personalEmailAddress,
            countryCode = countryCode,
            phoneNumber = phoneNumber,
            university = university,
            faculty = faculty,
            academicProgram = academicProgram,
            semester = semester,
            classSchedules = emptyList()
        )
    }
}