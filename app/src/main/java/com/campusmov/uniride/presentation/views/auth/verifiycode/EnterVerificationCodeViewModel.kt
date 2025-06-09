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

    var errorMessage = mutableStateOf("")
        private set

    init {
        getUserLocally()
    }

    fun onCodeInput(code: String) {
        state.value = state.value.copy(code = code.trim())
    }

    fun getUserLocally() {
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

    fun insertUserLocally(user: User) {
        viewModelScope.launch {
            val result = userUseCase.saveUserLocallyUseCase(user)
            when (result) {
                is Resource.Success -> {
                    Log.d("TAG", "insertUserLocally: ${result.data}")
                }
                is Resource.Failure -> {
                    errorMessage.value = "Error while saving user locally"
                }
                Resource.Loading -> {}
            }
        }
    }

    fun sendVerificationCode() {
        viewModelScope.launch {
            val result = authUseCase.verifyCode(state.value.code, user.value?.email ?: "")
            when(result) {
                is Resource.Success -> {
                    deleteAllUsersLocally()
                    Log.d("TAG", "sendVerificationCode: ${result.data}")
                    insertUserLocally(result.data.toDomain())
                }
                is Resource.Failure -> {
                    errorMessage.value = "Error al verificar el código"
                }
                Resource.Loading -> {}
            }
        }
    }

    fun deleteAllUsersLocally() {
        viewModelScope.launch {
            userUseCase.deleteAllUsersLocallyUseCase()
            Log.d("TAG", "All users deleted locally")
        }
    }
}