package com.campusmov.uniride.domain.profile.usecases

import com.campusmov.uniride.domain.profile.repository.ProfileClassScheduleRepository

class DeleteClassScheduleUseCase(
    private val profileClassScheduleRepository: ProfileClassScheduleRepository
) {
    suspend operator fun invoke(profileId: String, classScheduleId: String) =
        profileClassScheduleRepository.deleteClassSchedule(profileId, classScheduleId)
}