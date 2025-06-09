package com.campusmov.uniride.data.datasource.remote.dto

import com.campusmov.uniride.domain.profile.model.EGender
import com.campusmov.uniride.domain.profile.model.Profile
import retrofit2.http.Field
import java.time.LocalDate

data class ProfileResponseDto(
    @Field("id")
    val id: String?,
    // Contact Information
    @Field("institutionalEmailAddress")
    val institutionalEmailAddress: String?,
    @Field("personalEmailAddress")
    val personalEmailAddress: String?,
    @Field("countryCode")
    val countryCode: String?,
    @Field("phoneNumber")
    val phoneNumber: String?,
    // Personal Information
    @Field("firstName")
    val firstName: String?,
    @Field("lastName")
    val lastName: String?,
    @Field("birthDate")
    val birthDate: LocalDate?,
    @Field("gender")
    val gender: String?,
    @Field("profilePictureUrl")
    val profilePictureUrl: String?,
    // Academic Information
    @Field("university")
    val university: String?,
    @Field("faculty")
    val faculty: String?,
    @Field("academicProgram")
    val academicProgram: String?,
    @Field("semester")
    val semester: String?,
    // TODO: @Field("classSchedules")
) {
    fun toDomain(): Profile {
        return Profile(
            userId = id?: "",
            firstName = firstName ?: "",
            lastName = lastName ?: "",
            profilePictureUrl = profilePictureUrl ?: "https://img.freepik.com/vector-gratis/circulo-azul-usuario-blanco_78370-4707.jpg?semt=ais_items_boosted&w=740",
            birthDate = birthDate,
            gender = EGender.fromString(gender),
            personalEmailAddress = personalEmailAddress?: "",
            institutionalEmailAddress = institutionalEmailAddress ?: "",
            countryCode = countryCode ?: "+51",
            phoneNumber = phoneNumber ?: "",
            university = university ?: "",
            faculty = faculty ?: "",
            academicProgram = academicProgram ?: "",
            semester = semester ?: "",
            classSchedules = emptyList()
        )
    }
}