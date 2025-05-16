package com.campusmov.uniride.data.remote.dto

import retrofit2.http.Field

data class ProfileRequestDto(
    @Field("userId") val userId: String,
    @Field("institutionalEmailAddress") val institutionalEmailAddress: String,
    @Field("personalEmailAddress") val personalEmailAddress: String,
    @Field("countryCode") val countryCode: String,
    @Field("phoneNumber") val phoneNumber: String,
    @Field("firstName") val firstName: String,
    @Field("lastName") val lastName: String,
    @Field("birthDate") val birthDate: String,
    @Field("gender") val gender: String,
    @Field("profilePictureUrl") val profilePictureUrl: String,
    @Field("university") val university: String,
    @Field("faculty") val faculty: String,
    @Field("academicProgram") val academicProgram: String,
    @Field("semester") val semester: String,
    @Field("classSchedules") val classSchedules: List<ClassScheduleRequestDto>
)
