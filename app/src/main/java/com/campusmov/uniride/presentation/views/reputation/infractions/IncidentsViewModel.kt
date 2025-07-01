package com.campusmov.uniride.presentation.views.reputation.infractions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.usecases.UserUseCase
import com.campusmov.uniride.domain.reputation.model.Infraction
import com.campusmov.uniride.domain.reputation.usecases.ReputationIncentivesUseCase
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncidentsViewModel @Inject constructor(
    private val reputationUseCase: ReputationIncentivesUseCase,
    private val userUseCase: UserUseCase
): ViewModel(){

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _infractionsList = MutableStateFlow<List<Infraction>>(emptyList())
    val infractionsList: StateFlow<List<Infraction>> get() = _infractionsList

    init {
        getUserLocally()
    }

    private fun getUserLocally() {
        viewModelScope.launch {
            val result = userUseCase.getUserLocallyUseCase()
            when (result) {
                is Resource.Success -> {
                    _user.value = result.data
                    Log.d("TAG", "getUser: ${result.data}")
                    getUserInfractionsList()
                }
                is Resource.Failure -> {}
                Resource.Loading -> {}
            }
        }
    }

    fun getUserInfractionsList(){
        viewModelScope.launch {
            try {
                val userId = _user.value?.id
                userId?.let {
                    val result = reputationUseCase.getInfractionsOfUser(it)
                    when (result) {
                        is Resource.Success -> {
                            _infractionsList.value = result.data ?: emptyList()
                        }
                        is Resource.Failure -> {
                            Log.e("IncidentsViewModel", "Error fetching infractions: ${result.message}")
                        }
                        Resource.Loading -> {}
                    }
                }
            } catch (e: Exception) {
                Log.e("IncidentsViewModel", "Exception occurred while fetching infractions: ${e.message ?: "Unknown error"}")
            }
        }
    }


}