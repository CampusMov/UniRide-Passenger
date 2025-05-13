package com.campusmov.uniride.presentation.views.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.data.di.DataModule
import com.campusmov.uniride.data.repository.auth.AuthRepository
import kotlinx.coroutines.launch

class EnterInstitutionalEmailViewModel(private val authRepository: AuthRepository = DataModule.getAuthRepository()):ViewModel() {

    fun sendVerificationEmail(email: String, roleName: List<String>) {
        viewModelScope.launch {
            authRepository.sendVerificationEmail(email, roleName)
        }
    }
}