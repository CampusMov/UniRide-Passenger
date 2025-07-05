package com.campusmov.uniride.presentation.views.profile.registerprofile

import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.usecases.UserUseCase
import com.campusmov.uniride.domain.filemanagement.usecases.FileManagementUseCases
import com.campusmov.uniride.domain.location.model.PlacePrediction
import com.campusmov.uniride.domain.location.usecases.LocationUsesCases
import com.campusmov.uniride.domain.profile.model.EGender
import com.campusmov.uniride.domain.profile.usecases.ProfileUseCases
import com.campusmov.uniride.domain.shared.model.EDay
import com.campusmov.uniride.domain.shared.model.Location
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class RegisterProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val userUseCase: UserUseCase,
    private val locationUseCases: LocationUsesCases,
    private val fileManagementUseCases: FileManagementUseCases
) : ViewModel() {
    var profileState = mutableStateOf(RegisterProfileState())
        private set

    var currentClassScheduleState = mutableStateOf(CurrentClassScheduleState())
        private set

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    var registerProfileResponse = mutableStateOf<Resource<Unit>?>(null)
        private set

    var nextRecommendedStep = mutableIntStateOf(0)
        private set

    var isTermsAccepted = mutableStateOf(false)
        private set

    var isScheduleDialogOpen = mutableStateOf(false)
        private set

    private val _locationPredictions = MutableStateFlow<List<PlacePrediction>>(emptyList())
    val locationPredictions: StateFlow<List<PlacePrediction>> = _locationPredictions

    val isFullNameRegisterValid = derivedStateOf {
        profileState.value.firstName.isNotBlank() && profileState.value.lastName.isNotBlank()
    }

    val isTermsAcceptedValid = derivedStateOf { isTermsAccepted.value }

    val isPersonalInformationRegisterValid = derivedStateOf {
        profileState.value.firstName.isNotBlank() &&
                profileState.value.lastName.isNotBlank() &&
                profileState.value.birthDate != null
    }

    val isContactInformationRegisterValid = derivedStateOf {
        isValidPersonaEmailAddress() &&
                profileState.value.countryCode.isNotBlank() &&
                isValidPhoneNumber()
    }

    val isAcademicInformationRegisterValid = derivedStateOf {
        profileState.value.university.isNotBlank() &&
                profileState.value.faculty.isNotBlank() &&
                profileState.value.academicProgram.isNotBlank()
        isValidSemester()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val isCurrentClassScheduleValid = derivedStateOf {
        currentClassScheduleState.value.courseName.isNotBlank() &&
                currentClassScheduleState.value.startedAt != null &&
                currentClassScheduleState.value.endedAt != null &&
                currentClassScheduleState.value.selectedDay != null &&
                currentClassScheduleState.value.selectedLocation != null
    }

    val isRegisterProfileValid = derivedStateOf {
        isFullNameRegisterValid.value &&
                isTermsAcceptedValid.value &&
                isPersonalInformationRegisterValid.value &&
                isContactInformationRegisterValid.value &&
                isAcademicInformationRegisterValid.value
    }

    init {
        getUserLocally()
    }

    private fun getUserLocally() {
        viewModelScope.launch {
            when (val result = userUseCase.getUserLocallyUseCase()) {
                is Resource.Success -> {
                    _user.value = result.data
                    profileState.value = profileState.value.copy(
                        userId = result.data.id,
                        institutionalEmailAddress = result.data.email
                    )
                }

                is Resource.Failure -> {}
                Resource.Loading -> {}
            }
        }
    }

    fun isValidPersonaEmailAddress(): Boolean {
        val email = profileState.value.personalEmailAddress
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhoneNumber(): Boolean {
        val phone = profileState.value.phoneNumber
        return phone.isNotBlank() &&
                Patterns.PHONE.matcher(phone).matches() &&
                phone.length == 9
    }

    fun isValidSemester(): Boolean {
        val sem = profileState.value.semester
        return sem.isNotBlank() && sem.matches(Regex("^[0-9]{4}-[0-9]{2}$"))
    }

    fun onNextStep(nextStep: Int) {
        nextRecommendedStep.intValue = nextStep
    }

    fun onTermsAccepted(accepted: Boolean) {
        isTermsAccepted.value = accepted
    }

    fun onFirstNameInput(firstName: String) {
        profileState.value = profileState.value.copy(firstName = firstName)
    }

    fun onLastNameInput(lastName: String) {
        profileState.value = profileState.value.copy(lastName = lastName)
    }

    fun onBirthDateInput(birthDate: LocalDate) {
        profileState.value = profileState.value.copy(birthDate = birthDate)
    }

    fun onGenderInput(gender: EGender) {
        profileState.value = profileState.value.copy(gender = gender)
    }

    fun onPersonalEmailAddressInput(email: String) {
        profileState.value = profileState.value.copy(personalEmailAddress = email)
    }

    fun onPhoneNumberInput(number: String) {
        profileState.value = profileState.value.copy(phoneNumber = number)
    }

    fun onUniversityInput(university: String) {
        profileState.value = profileState.value.copy(university = university)
    }

    fun onFacultyInput(faculty: String) {
        profileState.value = profileState.value.copy(faculty = faculty)
    }

    fun onAcademicProgramInput(program: String) {
        profileState.value = profileState.value.copy(academicProgram = program)
    }

    fun onSemesterInput(semester: String) {
        profileState.value = profileState.value.copy(semester = semester)
    }

    fun uploadProfileImage(uri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fileName = user.value?.id
                    ?: (profileState.value.firstName + "_" + profileState.value.lastName)
                val url = fileManagementUseCases.uploadFileUseCase(uri, "images", fileName)
                profileState.value = profileState.value.copy(profilePictureUrl = url)
            } catch (e: Exception) {
                Log.e("RegisterProfileVM", "Error uploading profile image", e)
            }
            _isLoading.value = false
        }
    }

    fun onOpenDialogToAddNewSchedule() {
        currentClassScheduleState.value = CurrentClassScheduleState()
        isScheduleDialogOpen.value = true
    }

    fun onOpenDialogToEditSchedule(index: String) {
        val selectedClassSchedule = profileState.value.classSchedules.find { it.id == index }
        if (selectedClassSchedule == null) {
            Log.e("RegisterProfileVM", "Invalid schedule index: $index")
            return
        }
        currentClassScheduleState.value = currentClassScheduleState.value.copy(
            isEditing = true,
            editingIndex = index,
            courseName = selectedClassSchedule.courseName,
            selectedLocation = Location(
                name = selectedClassSchedule.locationName,
                latitude = selectedClassSchedule.latitude,
                longitude = selectedClassSchedule.longitude,
                address = selectedClassSchedule.address
            ),
            startedAt = selectedClassSchedule.startedAt,
            endedAt = selectedClassSchedule.endedAt,
            selectedDay = selectedClassSchedule.selectedDay
        )
        isScheduleDialogOpen.value = true
    }

    fun onCloseScheduleDialog() {
        currentClassScheduleState.value = CurrentClassScheduleState()
        isScheduleDialogOpen.value = false
        onScheduleLocationCleared()
    }

    fun onScheduleCourseNameInput(value: String) {
        currentClassScheduleState.value = currentClassScheduleState.value.copy(courseName = value)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onScheduleStartTimeInput(value: LocalTime) {
        if (currentClassScheduleState.value.endedAt != null && value.isAfter(
                currentClassScheduleState.value.endedAt
            )
        ) {
            Log.w("TAG", "Start time cannot be after end time")
            return
        }
        currentClassScheduleState.value = currentClassScheduleState.value.copy(startedAt = value)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onScheduleEndTimeInput(value: LocalTime) {
        if (currentClassScheduleState.value.startedAt != null && value.isBefore(
                currentClassScheduleState.value.startedAt
            )
        ) {
            Log.w("TAG", "End time cannot be before start time")
            return
        }
        currentClassScheduleState.value = currentClassScheduleState.value.copy(endedAt = value)
    }

    fun onScheduleDaySelected(day: EDay) {
        currentClassScheduleState.value = currentClassScheduleState.value.copy(selectedDay = day)
    }

    fun onScheduleLocationQueryChange(query: String) {
        viewModelScope.launch {
            _locationPredictions.value = locationUseCases.getPlacePredictions(query)
        }
    }

    fun onScheduleLocationSelected(placePrediction: PlacePrediction) {
        viewModelScope.launch {
            val place = locationUseCases.getPlaceDetails(placePrediction.id)
            val location = Location.fromPlace(place)
            currentClassScheduleState.value =
                currentClassScheduleState.value.copy(selectedLocation = location)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addClasScheduleToProfile() {
        if (!isCurrentClassScheduleValid.value) {
            Log.w("TAG", "Current class schedule is not valid")
            return
        }
        val newClassSchedule = currentClassScheduleState.value.toDomain()
        profileState.value =
            profileState.value.copy(classSchedules = profileState.value.classSchedules + newClassSchedule)
        onScheduleLocationCleared()
        onCloseScheduleDialog()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun editExistingClassSchedule() {
        if (currentClassScheduleState.value.editingIndex == null) {
            Log.e("TAG", "Editing index is null")
            return
        }
        if (!isCurrentClassScheduleValid.value) {
            Log.w("TAG", "Current class schedule is not valid")
            return
        }
        val updatedClassSchedule = currentClassScheduleState.value.toDomain()
        profileState.value = profileState.value.copy(
            classSchedules = profileState.value.classSchedules.map {
                if (it.id == currentClassScheduleState.value.editingIndex) {
                    updatedClassSchedule
                } else {
                    it
                }
            }
        )
        onScheduleLocationCleared()
        onCloseScheduleDialog()
    }

    fun onScheduleLocationCleared() {
        currentClassScheduleState.value =
            currentClassScheduleState.value.copy(selectedLocation = null)
        _locationPredictions.value = emptyList()
    }

    fun onDeleteSchedule() {
        val scheduleIdToDelete = currentClassScheduleState.value.editingIndex ?: run {
            Log.e("RegisterProfileVM", "Schedule ID (editingIndex) is null, cannot delete.")
            return
        }

        val profileId = profileState.value.userId
        if (profileId.isBlank()) {
            Log.e("RegisterProfileVM", "Profile ID is blank, cannot delete schedule.")
            return
        }

        val originalSchedules = profileState.value.classSchedules

        profileState.value = profileState.value.copy(
            classSchedules = originalSchedules.filterNot { it.id == scheduleIdToDelete }
        )

        onCloseScheduleDialog()
    }

    fun saveProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = profileUseCases.saveProfile(profileState.value.toDomain())) {
                is Resource.Success -> {
                    Log.d("RegisterProfileVM", "Profile saved")
                    registerProfileResponse.value = Resource.Success(Unit)
                }

                is Resource.Failure -> {
                    Log.e("RegisterProfileVM", "Error saving: ${result.message}")
                    registerProfileResponse.value = Resource.Failure(result.message)
                }

                Resource.Loading -> {}
            }
            _isLoading.value = false
        }
    }
}
