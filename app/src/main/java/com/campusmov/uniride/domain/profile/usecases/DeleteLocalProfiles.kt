package com.campusmov.uniride.domain.profile.usecases

import com.campusmov.uniride.domain.profile.repository.ProfileRepository

class DeleteLocalProfiles(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke() =
        profileRepository.deleteLocalProfiles()
}