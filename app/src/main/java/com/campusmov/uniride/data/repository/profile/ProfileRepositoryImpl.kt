package com.campusmov.uniride.data.repository.profile

import android.util.Log
import com.campusmov.uniride.data.model.mapper.toRequestBody
import com.campusmov.uniride.data.remote.profile.ProfileApiService
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.repository.ProfileRepository
import com.campusmov.uniride.domain.shared.util.Resource

class ProfileRepositoryImpl(
    private val api: ProfileApiService
) : ProfileRepository {

    override suspend fun saveProfile(profile: Profile): Resource<Unit> {
        return try {
            val response = api.saveProfile(profile.toRequestBody())
            if (response.isSuccessful) {
                Log.d("ProfileRepositoryImpl", "Profile saved successfully")
                Resource.Success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Log.e("ProfileRepositoryImpl", "Error saving profile: $errorMsg")
                Resource.Failure("Error saving profile: $errorMsg")
            }
        } catch (e: Exception) {
            Log.e("ProfileRepositoryImpl", "Exception saving profile: ${e.message}", e)
            Resource.Failure(e.message ?: "Unknown error")
        }
    }
}
