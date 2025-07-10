package com.campusmov.uniride.data.datasource.remote.service

import com.campusmov.uniride.data.datasource.remote.dto.ClassScheduleResponseDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileClassScheduleService {
    @GET("profile-service/profiles/{id}/class-schedules")
    suspend fun getClassSchedulesByProfileId(@Path("id") profileId: String): List<ClassScheduleResponseDto>

    @DELETE("profile-service/profiles/{profileId}/class-schedules/{classScheduleId}")
    suspend fun deleteClassSchedule(
        @Path("profileId") profileId: String,
        @Path("classScheduleId") classScheduleId: String
    ): Response<Unit>
}