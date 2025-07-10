package com.campusmov.uniride.data.repository.profile

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.campusmov.uniride.data.datasource.local.dao.ProfileDao
import com.campusmov.uniride.data.datasource.local.entities.toDomain
import com.campusmov.uniride.data.datasource.local.entities.toEntity
import com.campusmov.uniride.data.datasource.remote.mapper.toRequestBody
import com.campusmov.uniride.data.datasource.remote.service.ProfileService
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.repository.ProfileRepository
import com.campusmov.uniride.domain.shared.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class ProfileRepositoryImpl(
    private val profileService: ProfileService,
    private val profileDao: ProfileDao
) : ProfileRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun saveProfile(profile: Profile): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val response = profileService.saveProfile(profile.toRequestBody())
                if (response.isSuccessful) {
                    profileDao.insertOrUpdateProfile(profile.toEntity())
                    Log.d("TAG", "Profile saved successfully in API and local DB")
                    Resource.Success(Unit)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("TAG", "Error saving profile: $errorMsg")
                    Resource.Failure("Error saving profile: $errorMsg")
                }
            } catch (e: Exception) {
                Log.e("TAG", "Exception saving profile: ${e.message}", e)
                Resource.Failure(e.message ?: "Unknown error")
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getProfileById(profileId: String): Resource<Profile> =
        withContext(Dispatchers.IO) {
            try {
                val response = profileService.getProfileById(profileId)
                if (response.isSuccessful && response.body() != null) {
                    if (profileDao.hasLocalChanges(profileId) == 1) {
                        val response = profileService.updateProfile(
                            profileId, profileDao.getProfileById(profileId)
                                ?.toDomain()
                                ?.toRequestBody()
                                ?: throw IllegalStateException("Local profile not found for ID: $profileId")
                        )
                        if (response.isSuccessful) {
                            profileDao.updateLocalChanges(0, profileId)
                            Log.d("TAG", "Local changes synced with API for profile ID: $profileId")
                        } else {
                            Log.w(
                                "TAG",
                                "Failed to sync local changes with API for profile ID: $profileId"
                            )
                        }
                    }
                    val remoteProfile = profileService.getProfileById(profileId).body()!!.toDomain()
                    return@withContext Resource.Success(remoteProfile)
                }

                Log.w("TAG", "Failed to fetch from API. Trying local DB.")
                val localProfile = profileDao.getProfileById(profileId)
                if (localProfile != null) {
                    Log.d("TAG", "Profile fetched from local DB.")
                    return@withContext Resource.Success(localProfile.toDomain())
                } else {
                    Log.e("TAG", "Profile not found in API or local DB for ID: $profileId")
                    return@withContext Resource.Failure("Profile not found for ID: $profileId")
                }

            } catch (e: IOException) {
                Log.w("TAG", "Network error. Trying to fetch profile from local DB.")
                val localProfile = profileDao.getProfileById(profileId)
                return@withContext if (localProfile != null) {
                    Log.d("TAG", "Profile fetched from local DB due to network error.")
                    Resource.Success(localProfile.toDomain())
                } else {
                    Log.e("TAG", "Network error and no local profile found for ID: $profileId")
                    Resource.Failure("No network connection and no cached profile available.")
                }
            } catch (e: Exception) {
                Log.e("TAG", "Exception fetching profile: ${e.message}", e)
                return@withContext Resource.Failure(e.message ?: "Unknown error")
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateProfile(profileId: String, profile: Profile): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val response =
                    profileService.updateProfile(profileId, profile.toRequestBody(saveCase = false))
                if (response.isSuccessful) {
                    profileDao.insertOrUpdateProfile(profile.toEntity())
                    Log.d("TAG", "Profile updated successfully in API and local DB")
                    Resource.Success(Unit)
                } else {
                    profileDao.insertOrUpdateProfile(profile.toEntity())
                    profileDao.updateLocalChanges(1, profileId)
                    Log.d("TAG", "Profile updated in local DB despite API failure")
                    Resource.Success(Unit)
                }
            } catch (e: Exception) {
                profileDao.insertOrUpdateProfile(profile.toEntity())
                profileDao.updateLocalChanges(1, profileId)
                Log.d("TAG", "Profile updated in local DB despite API failure")
                Resource.Success(Unit)
            }
        }

    override suspend fun deleteLocalProfiles(): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                profileDao.deleteAllProfiles()
                Log.d("TAG", "Profile deleted successfully from API and local DB")
                Resource.Success(Unit)
            } catch (e: Exception) {
                Log.e("TAG", "Error deleting profile: ${e.message}", e)
                Resource.Failure(e.message ?: "Unknown error")
            }
        }
}