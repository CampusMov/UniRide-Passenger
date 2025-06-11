package com.campusmov.uniride.presentation.views.auth.verifiycode

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.model.toDomain
import com.campusmov.uniride.domain.auth.usecases.AuthUseCase
import com.campusmov.uniride.domain.auth.usecases.UserUseCase
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnterVerificationCodeViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase

): ViewModel() {

    var state = mutableStateOf(VerificationCodeValidationState())
        private set

    var email = mutableStateOf("")
        private set

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private var _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    var verifyCodeResponse = mutableStateOf<Resource<Unit>?>(null)
        private set

    private val _isLoading  = MutableStateFlow(false)
    val isLoading: MutableStateFlow<Boolean> get() = _isLoading

    private val _isButtonAvailable = MutableStateFlow(false)
    val isButtonAvailable: MutableStateFlow<Boolean> get() = _isButtonAvailable

    private val _secondsLeftToResendCode = MutableStateFlow(0)
    val secondsLeftToResendCode: MutableStateFlow<Int> get() = _secondsLeftToResendCode

    private val _isMessageErrorVisible = MutableStateFlow(false)
    val isMessageErrorVisible: StateFlow<Boolean> get() = _isMessageErrorVisible

    init {
        getUserLocally()
        startCountdown()
    }

    fun onCodeInput(code: String) {
        state.value = state.value.copy(code = code.trim())
    }

    private fun isCodeValid(): Boolean {
        return state.value.code.isNotEmpty()
                && state.value.code.length == 6
                && state.value.code.all { it.isDigit() }
    }

    private fun getUserLocally() {
        viewModelScope.launch {
            val result = userUseCase.getUserLocallyUseCase()
            when (result) {
                is Resource.Success -> {
                    _user.value = result.data
                    Log.d("TAG", "getUser: ${result.data}")
                }

                is Resource.Failure -> {}
                Resource.Loading -> {}
            }
        }
    }

    private fun insertUserLocally(user: User) {
        viewModelScope.launch {
            val result = userUseCase.saveUserLocallyUseCase(user)
            when (result) {
                is Resource.Success -> {
                    Log.d("TAG", "insertUserLocally: ${result.data}")
                }
                is Resource.Failure -> {
                    Log.e("TAG", "Error inserting user locally: ${result.message}")
                }
                Resource.Loading -> {}
            }
        }
    }

    fun sendVerificationCode() {
        if (!isCodeValid()) {
            showErrorMessage("El código debe tener 6 dígitos")
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _isButtonAvailable.value = false
            Log.d("TAG", "user values : ${user.value?.email} - ${state.value.code}")
            val result = authUseCase.verifyCode(state.value.code, user.value?.email ?: "")
            when(result) {
                is Resource.Success -> {
                    Log.d("TAG", "sendVerificationCode: ${result.data}")
                    deleteAllUsersLocally()
                    insertUserLocally(result.data.toDomain())
                    _isLoading.value = false
                    _isButtonAvailable.value = true
                    state.value.code = ""
                    verifyCodeResponse.value = Resource.Success(Unit)
                }
                is Resource.Failure -> {
                    Log.e("TAG", "Error verifying code: ${result.message}")
                    _isLoading.value = false
                    _isButtonAvailable.value = true
                    state.value.code = ""
                    showErrorMessage("Error al verificar el código")
                }
                is Resource.Loading -> {}
            }

        }
    }

    fun resendVerificationEmail() {
        if (_user.value?.email.isNullOrBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            _isButtonAvailable.value = false
            val result = authUseCase.verifyEmail(_user.value?.email.toString())
            when(result) {
                is Resource.Failure -> {
                    Log.e("TAG", "Error sending verification email: ${result.message}")
                    _isLoading.value = false
                    _isButtonAvailable.value = false
                    state.value.code = ""
                }
                is Resource.Success -> {
                    Log.d("TAG", "Verification email sent successfully")
                    startCountdown()
                    _isLoading.value = false
                    state.value.code = ""
                }
                is Resource.Loading -> {}
            }
        }
    }

    private fun deleteAllUsersLocally() {
        viewModelScope.launch {
            userUseCase.deleteAllUsersLocallyUseCase()
            Log.d("TAG", "All users deleted locally")
        }
    }

    private fun showErrorMessage(message: String) {
        _errorMessage.value = message
        _isMessageErrorVisible.value = true
        viewModelScope.launch {
            delay(5_000L)
            _isMessageErrorVisible.value = false
            _errorMessage.value = ""
        }
    }

    private fun startCountdown() {
        viewModelScope.launch {
            resetCountdown()
            for (i in 60 downTo 0) {
                _secondsLeftToResendCode.value = i
                delay(1_000L)
                if (i == 0) {
                    _isButtonAvailable.value = true
                }
            }
            _secondsLeftToResendCode.value = 0
        }
    }

    private fun resetCountdown() {
        _secondsLeftToResendCode.value = 60
        _isButtonAvailable.value = false
    }
}