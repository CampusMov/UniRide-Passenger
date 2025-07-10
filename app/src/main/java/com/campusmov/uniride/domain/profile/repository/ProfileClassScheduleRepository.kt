package com.campusmov.uniride.domain.profile.repository

import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.domain.shared.util.Resource

interface ProfileClassScheduleRepository {
    suspend fun getClassScheduleByProfileId(profileId: String): Resource<List<ClassSchedule>>
    suspend fun deleteClassSchedule(
        profileId: String,
        classScheduleId: String
    ): Resource<Unit>
}