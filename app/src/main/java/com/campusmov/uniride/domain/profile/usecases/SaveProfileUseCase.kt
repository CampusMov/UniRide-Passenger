package com.campusmov.uniride.domain.profile.usecases

import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.repository.ProfileRepository
import com.campusmov.uniride.domain.shared.util.Resource

class SaveProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(profile: Profile): Resource<Unit> {
        return profileRepository.saveProfile(profile)
    }
}
