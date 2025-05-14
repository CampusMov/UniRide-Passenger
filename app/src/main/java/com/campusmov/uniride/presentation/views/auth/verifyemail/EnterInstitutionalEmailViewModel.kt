package com.campusmov.uniride.presentation.views.auth.verifyemail

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.auth.model.AuthEmailVerificationResponse
import com.campusmov.uniride.domain.auth.usecases.AuthUseCase
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnterInstitutionalEmailViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
):ViewModel() {

    var state = mutableStateOf(InstitutionalEmailValidationState())
        private set

    var errorMessage = mutableStateOf("")
        private set

    var verifyEmailResponse = mutableStateOf<Resource<Unit>?>(null)
        private set

    fun onEmailInput(email: String) {
        state.value = state.value.copy(email = email.trim())
    }

    fun isEmailValid(): Boolean {
        val emailPattern = Patterns.EMAIL_ADDRESS.matcher(state.value.email).matches()
        val universityDomainPattern = Regex("[a-zA-Z0-9]+\\.edu\\.pe$").matches(state.value.email.substringAfter("@"))
        return emailPattern && universityDomainPattern
    }

    fun sendVerificationEmail() = viewModelScope.launch {
        if (isEmailValid()) {
            errorMessage.value = ""
            val result = authUseCase.verifyEmail(state.value.email)
            if (result is Resource.Success) {
                //verifyEmailResponse = mutableStateOf(result)
                verifyEmailResponse.value = result
            }
        } else {
            errorMessage.value = "Formato de correo inválido"
            Log.d("TAG", "Invalid email format ${state.value.email}")
        }
    }


}