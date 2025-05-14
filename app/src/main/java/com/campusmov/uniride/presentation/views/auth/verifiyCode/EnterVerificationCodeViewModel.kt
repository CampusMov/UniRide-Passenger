package com.campusmov.uniride.presentation.views.auth.verifiyCode

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.common.GlobalVariables.ROLE
import com.campusmov.uniride.data.datasource.local.datastore.LocalDataStore
import com.campusmov.uniride.domain.auth.model.AuthVerificationCodeResponse
import com.campusmov.uniride.domain.auth.usecases.AuthUseCase
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnterVerificationCodeViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val localDataStore: LocalDataStore

):ViewModel() {

    var state = mutableStateOf(VerificationCodeValidationState())
        private set

    var email = mutableStateOf("")
        private set

    var errorMessage = mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            email.value = localDataStore.getEmail().first() ?: ""
        }
    }


    fun onCodeInput(code: String) {
        state.value = state.value.copy(code = code.trim())
    }

    init {
        getSessionData()
    }

    fun saveSession(authVerificationCodeResponse: AuthVerificationCodeResponse) =viewModelScope.launch {
        authUseCase.saveSession(authVerificationCodeResponse)
    }

    fun getSessionData() = viewModelScope.launch {
        authUseCase.getSessionData().collect(){
            Log.d( "TAG", "getSessionData: $it")
        }
    }

    fun getEmail() = viewModelScope.launch {
        localDataStore.getEmail().collect { email ->
            state.value = state.value.copy(email = email ?: "")
        }
    }


    fun sendVerificationCode() = viewModelScope.launch {
        val result = authUseCase.verifyCode(state.value.code, ROLE)
        if (result is Resource.Success) {
            saveSession(result.data)
        } else {
            errorMessage.value = "Código inválido"
        }
    }
}