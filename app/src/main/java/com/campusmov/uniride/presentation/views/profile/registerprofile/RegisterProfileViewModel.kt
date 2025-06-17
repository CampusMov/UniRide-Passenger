package com.campusmov.uniride.presentation.views.profile.registerprofile

import android.net.Uri
import android.util.Log
import android.util.Patterns
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
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class RegisterProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val userUseCase: UserUseCase,
    private val locationUseCases: LocationUsesCases,
    private val fileManagementUseCases: FileManagementUseCases
) : ViewModel() {
    var state = mutableStateOf(RegisterProfileState())
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
        state.value.firstName.isNotBlank() && state.value.lastName.isNotBlank()
    }

    val isTermsAcceptedValid = derivedStateOf { isTermsAccepted.value }

    val isPersonalInformationRegisterValid = derivedStateOf {
        state.value.firstName.isNotBlank() &&
                state.value.lastName.isNotBlank() &&
                state.value.birthDate != null
    }

    val isContactInformationRegisterValid = derivedStateOf {
                isValidPersonaEmailAddress() &&
                state.value.countryCode.isNotBlank() &&
                isValidPhoneNumber()
    }

    val isAcademicInformationRegisterValid = derivedStateOf {
        state.value.university.isNotBlank() &&
                state.value.faculty.isNotBlank() &&
                state.value.academicProgram.isNotBlank()
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
                    state.value = state.value.copy(
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
        val email = state.value.personalEmailAddress
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhoneNumber(): Boolean {
        val phone = state.value.phoneNumber
        return phone.isNotBlank() &&
                Patterns.PHONE.matcher(phone).matches() &&
                phone.length == 9
    }

    fun isValidSemester(): Boolean {
        val sem = state.value.semester
        return sem.isNotBlank() && sem.matches(Regex("^[0-9]{4}-[0-9]{2}$"))
    }

    fun onNextStep(nextStep: Int) {
        nextRecommendedStep.intValue = nextStep
    }

    fun onTermsAccepted(accepted: Boolean) {
        isTermsAccepted.value = accepted
    }

    fun onFirstNameInput(firstName: String) {
        state.value = state.value.copy(firstName = firstName)
    }

    fun onLastNameInput(lastName: String) {
        state.value = state.value.copy(lastName = lastName)
    }

    fun onProfilePictureUrlInput(url: String) {
        state.value = state.value.copy(profilePictureUrl = url)
    }

    fun onBirthDateInput(birthDate: LocalDate) {
        state.value = state.value.copy(birthDate = birthDate)
    }

    fun onGenderInput(gender: EGender) {
        state.value = state.value.copy(gender = gender)
    }

    fun onPersonalEmailAddressInput(email: String) {
        state.value = state.value.copy(personalEmailAddress = email)
    }

    fun onPhoneNumberInput(number: String) {
        state.value = state.value.copy(phoneNumber = number)
    }

    fun onUniversityInput(university: String) {
        state.value = state.value.copy(university = university)
    }

    fun onFacultyInput(faculty: String) {
        state.value = state.value.copy(faculty = faculty)
    }

    fun onAcademicProgramInput(program: String) {
        state.value = state.value.copy(academicProgram = program)
    }

    fun uploadProfileImage(uri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fileName = user.value?.id ?: (state.value.firstName + "_" + state.value.lastName)
                val url = fileManagementUseCases.uploadFileUseCase(uri, "images", fileName)
                state.value = state.value.copy(profilePictureUrl = url)
            } catch (e: Exception) {
                Log.e("RegisterProfileVM", "Error uploading profile image", e)
            }
            _isLoading.value = false
        }
    }

    // Schedule dialog handlers
    fun onAddScheduleDialog() {
        state.value = state.value.copy(
            scheduleDialogState = ClassScheduleDialogState(isEditing = false)
        )
        isScheduleDialogOpen.value = true
    }

    fun onEditScheduleDialog(index: Int) {
        val sched = state.value.classSchedules[index]
        state.value = state.value.copy(
            scheduleDialogState = ClassScheduleDialogState(
                isEditing = true,
                editingIndex = index,
                courseName = sched.courseName,
                selectedLocation = Location(
                    name = sched.locationName,
                    latitude = sched.latitude,
                    longitude = sched.longitude,
                    address = sched.address
                ),
                startedAt = sched.startedAt,
                endedAt = sched.endedAt,
                selectedDay = sched.selectedDay
            )
        )
        isScheduleDialogOpen.value = true
    }

    fun onCancelScheduleDialog() {
        state.value = state.value.copy(
            scheduleDialogState = ClassScheduleDialogState()
        )
        isScheduleDialogOpen.value = false
    }

    fun onScheduleCourseNameInput(value: String) {
        state.value = state.value.copy(
            scheduleDialogState = state.value.scheduleDialogState.copy(courseName = value)
        )
    }

    fun onScheduleStartTimeInput(value: LocalDateTime) {
        state.value = state.value.copy(
            scheduleDialogState = state.value.scheduleDialogState.copy(startedAt = value)
        )
    }

    fun onScheduleEndTimeInput(value: LocalDateTime) {
        state.value = state.value.copy(
            scheduleDialogState = state.value.scheduleDialogState.copy(endedAt = value)
        )
    }

    fun onScheduleDaySelected(day: EDay) {
        state.value = state.value.copy(
            scheduleDialogState = state.value.scheduleDialogState.copy(selectedDay = day)
        )
    }

    fun onScheduleLocationQueryChange(query: String) {
        viewModelScope.launch {
            _locationPredictions.value = locationUseCases.getPlacePredictions(query)
        }
    }

    fun onScheduleLocationSelected(pred: PlacePrediction) {
        viewModelScope.launch {
            val place = locationUseCases.getPlaceDetails(pred.id)
            val loc = Location.fromPlace(place)
            state.value = state.value.copy(
                scheduleDialogState = state.value.scheduleDialogState.copy(selectedLocation = loc)
            )
        }
    }

    fun addOrUpdateSchedule() {
        val dlg = state.value.scheduleDialogState
        val newSched = dlg.toDomain()
        val list = state.value.classSchedules.toMutableList().apply {
            if (dlg.isEditing && dlg.editingIndex != null) set(dlg.editingIndex, newSched)
            else add(newSched)
        }
        state.value = state.value.copy(
            classSchedules = list,
            scheduleDialogState = ClassScheduleDialogState()
        )
        onCancelScheduleDialog()
    }

    fun onScheduleLocationCleared() {
        state.value = state.value.copy(
            scheduleDialogState = state.value.scheduleDialogState.copy(selectedLocation = null)
        )
    }

    fun deleteSchedule() {
        val idx = state.value.scheduleDialogState.editingIndex
        if (idx != null) {
            val list = state.value.classSchedules.filterIndexed { i, _ -> i != idx }
            state.value = state.value.copy(
                classSchedules = list,
                scheduleDialogState = ClassScheduleDialogState()
            )
        }
        onCancelScheduleDialog()
    }

    fun saveProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = profileUseCases.saveProfile(state.value.toDomain())) {
                is Resource.Success -> {
                    Log.d("RegisterProfileVM", "Profile saved")
                    registerProfileResponse.value = Resource.Success(Unit)
                }

                is Resource.Failure -> {
                    Log.e("RegisterProfileVM", "Error saving: ${result.message}")
                    registerProfileResponse.value = Resource.Failure(result.message ?: "Error")
                }

                Resource.Loading -> {}
            }
            _isLoading.value = false
        }
    }
}
