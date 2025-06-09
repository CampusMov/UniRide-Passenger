package com.campusmov.uniride.data.repository.profile

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.campusmov.uniride.data.datasource.remote.mapper.toRequestBody
import com.campusmov.uniride.data.datasource.remote.service.ProfileService
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.repository.ProfileRepository
import com.campusmov.uniride.domain.shared.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(
    private val profileService: ProfileService
) : ProfileRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun saveProfile(profile: Profile): Resource<Unit> = withContext(Dispatchers.IO){
        return@withContext try {
            val response = profileService.saveProfile(profile.toRequestBody())
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
