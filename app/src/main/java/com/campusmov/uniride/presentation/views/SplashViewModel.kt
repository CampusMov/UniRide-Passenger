package com.campusmov.uniride.presentation.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.auth.repository.UserRepository
import com.campusmov.uniride.domain.profile.repository.ProfileRepository
import com.campusmov.uniride.domain.shared.util.Resource
import com.campusmov.uniride.presentation.navigation.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination

    init {
        checkUserAndProfileStatus()
    }

    private fun checkUserAndProfileStatus() {
        viewModelScope.launch {
            val route = when (val userResource = userRepository.getUserLocally()) {
                is Resource.Success -> {
                    when (profileRepository.getProfileById(userResource.data.id)) {
                        is Resource.Success -> Graph.MATCHING
                        is Resource.Failure -> Graph.PROFILE
                        else -> null
                    }
                }
                is Resource.Failure -> Graph.AUTH
                else -> null
            }
            _startDestination.value = route
        }
    }
}