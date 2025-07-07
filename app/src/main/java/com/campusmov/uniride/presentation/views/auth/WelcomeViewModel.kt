package com.campusmov.uniride.presentation.views.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.usecases.AuthUseCase
import com.campusmov.uniride.domain.auth.usecases.UserUseCase
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val authUseCase: AuthUseCase
): ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    init {
        getUserLocally()
    }

    private fun getUserLocally() {
        viewModelScope.launch {
            val result = userUseCase.getUserLocallyUseCase()
            when (result) {
                is Resource.Success -> {
                    Log.d("TAG", "WelcomeVM: local user: ${result.data}")
                    result.data.let { getUserById(it.id) }
                }
                is Resource.Failure -> {}
                Resource.Loading -> {}
            }
        }
    }

    private fun getUserById(userId: String) {
        viewModelScope.launch {
            val result = authUseCase.getUserById(userId)
            when (result) {
                is Resource.Success -> {
                    _user.value = result.data
                    Log.d("TAG", "WelcomeVM: getUserById: ${result.data}")
                }
                is Resource.Failure -> {}
                Resource.Loading -> {}
            }
        }
    }


}