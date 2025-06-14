package com.campusmov.uniride.data.datasource.remote.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.campusmov.uniride.domain.profile.model.EGender
import com.campusmov.uniride.domain.profile.model.Profile
import retrofit2.http.Field
import java.time.LocalDate

data class ProfileRequestDto(
    @Field("userId")
    val userId: String,
    @Field("institutionalEmailAddress")
    val institutionalEmailAddress: String,
    @Field("personalEmailAddress")
    val personalEmailAddress: String,
    @Field("countryCode")
    val countryCode: String,
    @Field("phoneNumber")
    val phoneNumber: String,
    @Field("firstName")
    val firstName: String,
    @Field("lastName")
    val lastName: String,
    @Field("birthDate")
    val birthDate: String,
    @Field("gender")
    val gender: EGender,
    @Field("profilePictureUrl")
    val profilePictureUrl: String,
    @Field("university")
    val university: String,
    @Field("faculty")
    val faculty: String,
    @Field("academicProgram")
    val academicProgram: String,
    @Field("semester")
    val semester: String,
    @Field("classSchedules")
    val classSchedules: List<ClassScheduleRequestDto>
) {
    companion object  {
        @RequiresApi(Build.VERSION_CODES.O)
        fun fromDomain(profile: Profile): ProfileRequestDto {
            return ProfileRequestDto(
                userId = profile.userId,
                institutionalEmailAddress = profile.institutionalEmailAddress,
                personalEmailAddress = profile.personalEmailAddress,
                countryCode = profile.countryCode,
                phoneNumber = profile.phoneNumber,
                firstName = profile.firstName,
                lastName = profile.lastName,
                birthDate = profile.birthDate?.toString() ?: LocalDate.now().toString(),
                gender = profile.gender,
                profilePictureUrl = profile.profilePictureUrl,
                university = profile.university,
                faculty = profile.faculty,
                academicProgram = profile.academicProgram,
                semester = profile.semester,
                classSchedules = profile.classSchedules.map { ClassScheduleRequestDto.fromDomain(it) }
            )
        }
    }
}
