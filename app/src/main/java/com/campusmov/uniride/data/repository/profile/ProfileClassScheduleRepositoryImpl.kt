package com.campusmov.uniride.data.repository.profile

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.campusmov.uniride.data.datasource.remote.mapper.toRequestBody
import com.campusmov.uniride.data.datasource.remote.service.ProfileClassScheduleService
import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.domain.profile.repository.ProfileClassScheduleRepository
import com.campusmov.uniride.domain.shared.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class ProfileClassScheduleRepositoryImpl(
    private val profileClassScheduleService: ProfileClassScheduleService
): ProfileClassScheduleRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getClassScheduleByProfileId(profileId: String): Resource<List<ClassSchedule>> = withContext(Dispatchers.IO) {
        try {
            val classSchedulesListResponse = profileClassScheduleService.getClassSchedulesByProfileId(profileId)
            if (classSchedulesListResponse.isEmpty()) {
                Log.d("TAG", "No class schedules found for profile ID: $profileId")
                Resource.Failure("No class schedules found for profile ID: $profileId")
            } else {
                Log.d("TAG", "Class schedules fetched successfully for profile ID: $profileId")
                Resource.Success(classSchedulesListResponse.map { it.toDomain() })
            }
        }  catch (e: IOException) {
            Log.e("TAG", "Network error while fetching class schedules for profile ID: $profileId", e)
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("TAG", "Unexpected error while fetching class schedules for profile ID: $profileId", e)
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun deleteClassSchedule(
        profileId: String,
        classScheduleId: String
    ): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = profileClassScheduleService.deleteClassSchedule(profileId, classScheduleId)
            if (response.isSuccessful) {
                Log.d("TAG", "Class schedule deleted successfully for profile ID: $profileId, classScheduleId: $classScheduleId")
                Resource.Success(Unit)
            } else {
                Log.e("TAG", "Failed to delete class schedule: ${response.errorBody()?.string()}")
                Resource.Failure("Failed to delete class schedule: ${response.errorBody()?.string()}")
            }
        } catch (e: IOException) {
            Log.e("TAG", "Network error while deleting class schedule for profile ID: $profileId", e)
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("TAG", "Unexpected error while deleting class schedule for profile ID: $profileId", e)
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }

}