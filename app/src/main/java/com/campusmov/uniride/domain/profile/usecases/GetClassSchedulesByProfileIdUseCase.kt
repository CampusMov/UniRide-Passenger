package com.campusmov.uniride.domain.profile.usecases

import com.campusmov.uniride.domain.profile.repository.ProfileClassScheduleRepository

class GetClassSchedulesByProfileIdUseCase(private val profileClassScheduleRepository: ProfileClassScheduleRepository) {
    suspend operator fun invoke(profileId: String) = profileClassScheduleRepository.getClassScheduleByProfileId(profileId)
}