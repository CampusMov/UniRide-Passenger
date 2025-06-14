package com.campusmov.uniride.domain.profile.usecases

import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.repository.ProfileRepository

class SaveProfileUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(profile: Profile) = profileRepository.saveProfile(profile)
}
