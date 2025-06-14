package com.campusmov.uniride.domain.profile.repository

import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.shared.util.Resource

interface ProfileRepository {
    suspend fun saveProfile(profile: Profile): Resource<Unit>
}