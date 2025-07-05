package com.campusmov.uniride.data.datasource.remote.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.campusmov.uniride.data.datasource.remote.dto.ProfileRequestDto
import com.campusmov.uniride.domain.profile.model.Profile
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun Profile.toRequestBody(saveCase: Boolean = true): ProfileRequestDto =
    ProfileRequestDto(
        userId = if (saveCase) userId else null,
        institutionalEmailAddress,
        personalEmailAddress,
        countryCode,
        phoneNumber,
        firstName,
        lastName,
        birthDate?.toString() ?: LocalDate.now().toString(),
        gender,
        profilePictureUrl,
        university,
        faculty,
        academicProgram,
        semester,
        classSchedules.map { it.toRequestBody() }
    )