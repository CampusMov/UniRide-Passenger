package com.campusmov.uniride.presentation.views.auth.verifyemail

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.usecases.AuthUseCase
import com.campusmov.uniride.domain.auth.usecases.UserUseCase
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnterInstitutionalEmailViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase
):ViewModel() {

    var state = mutableStateOf(InstitutionalEmailValidationState())
        private set

    var verifyEmailResponse = mutableStateOf<Resource<Unit>?>(null)
        private set

    private val _isLoading  = MutableStateFlow(false)
    val isLoading: MutableStateFlow<Boolean> get() = _isLoading

    private val _isButtonAvailable = MutableStateFlow(false)
    val isButtonAvailable: MutableStateFlow<Boolean> get() = _isButtonAvailable

    fun onEmailInput(email: String) {
        state.value = state.value.copy(email = email.trim())
        _isButtonAvailable.value = isEmailValid()
    }

    private fun isEmailValid(): Boolean {
        val emailPattern = Patterns.EMAIL_ADDRESS.matcher(state.value.email).matches()
        val universityDomainPattern = Regex("[a-zA-Z0-9]+\\.edu\\.pe$").matches(state.value.email.substringAfter("@"))
        return emailPattern && universityDomainPattern
    }

    fun sendVerificationEmail() {
        viewModelScope.launch {
            deleteAllUsersLocally()
            _isLoading.value = true
            _isButtonAvailable.value = false
            val result = authUseCase.verifyEmail(state.value.email)
            when(result) {
                is Resource.Failure -> {
                    Log.e("TAG", "Error sending verification email: ${result.message}")
                    _isLoading.value = false
                    _isButtonAvailable.value = true
                    state.value.email = ""
                }
                is Resource.Success -> {
                    Log.d("TAG", "Verification email sent successfully")
                    deleteAllUsersLocally()
                    verifyEmailResponse.value = result
                    saveUserLocally(state.value.email)
                    _isLoading.value = false
                    _isButtonAvailable.value = false
                    state.value.email = ""
                }
                is Resource.Loading -> {}
            }
        }
    }

    private fun saveUserLocally(email: String) {
        val user = User(email = email)
        viewModelScope.launch {
            userUseCase.saveUserLocallyUseCase(user)
            Log.d("TAG", "User saved locally: ${user.email}")
        }
    }

    private fun deleteAllUsersLocally() {
        viewModelScope.launch {
            userUseCase.deleteAllUsersLocallyUseCase()
            Log.d("TAG", "All users deleted locally")
        }
    }
}