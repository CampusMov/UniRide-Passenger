package com.campusmov.uniride.domain.profile.usecases

import com.campusmov.uniride.domain.profile.repository.ProfileRepository

class GetProfileByIdUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(profileId: String) = profileRepository.getProfileById(profileId)
}