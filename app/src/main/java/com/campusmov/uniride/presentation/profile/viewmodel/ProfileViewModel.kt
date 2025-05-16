package com.campusmov.uniride.presentation.profile.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.auth.usecases.AuthUseCase
import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.usecases.ProfileUseCases
import com.campusmov.uniride.domain.shared.util.Resource
import com.campusmov.uniride.presentation.profile.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val authUseCase: AuthUseCase
) : ViewModel() {

    var personalInfo = mutableStateOf(PersonalInfoUiModel())
        private set

    var contactInfo = mutableStateOf(ContactInfoUiModel())
        private set

    var academicInfo = mutableStateOf(AcademicInfoUiModel())
        private set

    var terms = mutableStateOf(TermsUiModel())
        private set

    var editingSchedule = mutableStateOf<ClassScheduleUiModel?>(null)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    var saveProfileState = mutableStateOf<Resource<Unit>?>(null)
        private set

    var profileTextToShow = mutableStateOf<String?>(null)
        private set

    private var userId: String = ""

    init {
        viewModelScope.launch {
            authUseCase.getSessionData().collect { sessionData ->
                userId = sessionData.id ?: ""
                val email = sessionData.email ?: ""
                contactInfo.value = contactInfo.value.copy(institutionalEmail = email)
            }
        }
    }

    fun buildProfile(): Profile? {
        if (userId.isBlank()) {
            errorMessage.value = "User ID is not available."
            return null
        }
        return Profile(
            userId = userId,
            institutionalEmailAddress = contactInfo.value.institutionalEmail,
            personalEmailAddress = contactInfo.value.personalEmail,
            countryCode = contactInfo.value.countryCode.ifBlank { "+51" },
            phoneNumber = contactInfo.value.phoneNumber,
            firstName = personalInfo.value.firstName,
            lastName = personalInfo.value.lastName,
            birthDate = personalInfo.value.birthDate,
            gender = personalInfo.value.gender,
            profilePictureUrl = personalInfo.value.profilePicUri ?: "",
            university = academicInfo.value.university,
            faculty = academicInfo.value.faculty,
            academicProgram = academicInfo.value.academicProgram,
            semester = academicInfo.value.semester,
            classSchedules = academicInfo.value.schedules.map {
                ClassSchedule(
                    courseName = it.name,
                    locationName = it.locationName,
                    latitude = 0.0,
                    longitude = 0.0,
                    address = it.address,
                    startedAt = it.start,
                    endedAt = it.end,
                    selectedDay = it.dayOfWeek
                )
            }
        )
    }

    fun onSaveProfile() {
        val profile = buildProfile() ?: return

        viewModelScope.launch {
            saveProfileState.value = Resource.Loading
            saveProfileState.value = try {
                profileUseCases.saveProfile(profile)
            } catch (e: Exception) {
                Resource.Failure(e.message ?: "Unknown error")
            }
        }
    }

    fun prepareProfileText() {
        val profile = buildProfile() ?: return
        profileTextToShow.value = profile.toString()
    }

    fun updatePersonal(update: PersonalInfoUiModel.() -> PersonalInfoUiModel) {
        personalInfo.value = personalInfo.value.update()
    }

    fun updateContact(update: ContactInfoUiModel.() -> ContactInfoUiModel) {
        contactInfo.value = contactInfo.value.update()
    }

    fun updateAcademic(update: AcademicInfoUiModel.() -> AcademicInfoUiModel) {
        academicInfo.value = academicInfo.value.update()
    }

    fun updateTerms(update: TermsUiModel.() -> TermsUiModel) {
        terms.value = terms.value.update()
    }

    fun addSchedule(item: ClassScheduleUiModel) {
        val updatedSchedules = academicInfo.value.schedules.toMutableList().apply { add(item) }
        academicInfo.value = academicInfo.value.copy(schedules = updatedSchedules)
    }

    fun deleteSchedule(item: ClassScheduleUiModel) {
        val updatedSchedules = academicInfo.value.schedules.filterNot { it == item }
        academicInfo.value = academicInfo.value.copy(schedules = updatedSchedules)
    }

    fun startEditingSchedule(item: ClassScheduleUiModel) {
        editingSchedule.value = item
    }

    fun finishEditingSchedule(updated: ClassScheduleUiModel) {
        val updatedSchedules = academicInfo.value.schedules.map {
            if (it == editingSchedule.value) updated else it
        }
        academicInfo.value = academicInfo.value.copy(schedules = updatedSchedules)
        editingSchedule.value = null
    }
}

