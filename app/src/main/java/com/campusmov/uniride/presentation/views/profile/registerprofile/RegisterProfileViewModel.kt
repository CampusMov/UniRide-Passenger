package com.campusmov.uniride.presentation.views.profile.registerprofile

import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.profile.model.EGender
import com.campusmov.uniride.domain.profile.usecases.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RegisterProfileViewModel @Inject constructor (
    private val profileUseCases: ProfileUseCases
): ViewModel(){

    var state = mutableStateOf(RegisterProfileState())
        private set

    var nextRecommendedStep = mutableIntStateOf(0)
        private set

    var isTermsAccepted = mutableStateOf(false)
        private set

    val isFullNameRegisterValid = derivedStateOf {
        state.value.firstName.isNotBlank() && state.value.lastName.isNotBlank()
    }

    val isTermsAcceptedValid = derivedStateOf {
        isTermsAccepted.value
    }

    val isPersonalInformationRegisterValid = derivedStateOf {
        // TODO: state.value.profilePictureUrl.isNotBlank() &&
        state.value.firstName.isNotBlank() &&
        state.value.lastName.isNotBlank() &&
        state.value.birthDate != null
    }

    val isContactInformationRegisterValid = derivedStateOf {
        state.value.institutionalEmailAddress.isNotBlank() &&
                isValidPersonaEmailAddress() &&
                state.value.countryCode.isNotBlank() &&
                isValidPhoneNumber()
    }

    val isAcademicInformationRegisterValid = derivedStateOf {
        state.value.university.isNotBlank() &&
                state.value.faculty.isNotBlank() &&
                state.value.academicProgram.isNotBlank() &&
                isValidSemester()
    }

    fun isValidPersonaEmailAddress(): Boolean {
        val email = state.value.personalEmailAddress
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhoneNumber(): Boolean {
        val phoneNumber = state.value.phoneNumber
        return phoneNumber.isNotBlank() && Patterns.PHONE.matcher(phoneNumber).matches() && phoneNumber.length == 9
    }

    fun isValidSemester(): Boolean {
        val semester = state.value.semester
        return semester.isNotBlank() && semester.matches(Regex("^[0-9]{4}-[0-9]{2}$"))
    }

    fun onNextStep(nextStep: Int){
        if (nextStep > 0) {
            nextRecommendedStep.intValue = nextStep
        }
        if (nextStep == -1) {
            nextRecommendedStep.intValue = -1
        }
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

    fun onProfilePictureUrlInput(profilePictureUrl: String) {
        state.value = state.value.copy(profilePictureUrl = profilePictureUrl)
    }

    fun onBirthDateInput(birthDate: LocalDate) {
        state.value = state.value.copy(birthDate = birthDate)
    }

    fun onGenderInput(gender: EGender){
        state.value = state.value.copy(gender = gender)
    }

    fun onPersonalEmailAddressInput(personalEmailAddress: String) {
        state.value = state.value.copy(personalEmailAddress = personalEmailAddress)
    }

    fun onPhoneNumberInput(phoneNumber: String) {
        state.value = state.value.copy(phoneNumber = phoneNumber)
    }

    fun onUniversityInput(university: String) {
        state.value = state.value.copy(university = university)
    }

    fun onFacultyInput(faculty: String) {
        state.value = state.value.copy(faculty = faculty)
    }

    fun onAcademicProgramInput(academicProgram: String) {
        state.value = state.value.copy(academicProgram = academicProgram)
    }

    fun onSemesterInput(semester: String) {
        state.value = state.value.copy(semester = semester)
    }

    fun saveProfile(){
        viewModelScope.launch {
            val profile = state.value.toDomain()
            profileUseCases.saveProfile(profile)
        }
    }
}