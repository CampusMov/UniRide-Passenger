package com.campusmov.uniride.domain.profile.usecases

import android.os.Build
import androidx.annotation.RequiresApi
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.repository.ProfileClassScheduleRepository
import com.campusmov.uniride.domain.profile.repository.ProfileRepository
import com.campusmov.uniride.domain.shared.util.Resource

class UpdateProfileUsecase(
    private val profileRepository: ProfileRepository,
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(profile: Profile): Resource<Unit> {
        val profileResult = profileRepository.updateProfile(profile.userId, profile)
        if (profileResult is Resource.Failure) return profileResult
        return Resource.Success(Unit)
    }
}