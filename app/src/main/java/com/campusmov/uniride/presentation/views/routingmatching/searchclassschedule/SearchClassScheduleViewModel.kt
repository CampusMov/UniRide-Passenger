package com.campusmov.uniride.presentation.views.routingmatching.searchclassschedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.usecases.UserUseCase
import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.domain.profile.usecases.ProfileClassScheduleUseCases
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchClassScheduleViewModel @Inject constructor(
    private val profileClassScheduleUseCases: ProfileClassScheduleUseCases,
    private val userUseCase: UserUseCase
): ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _classSchedules = MutableStateFlow<List<ClassSchedule>>(emptyList())

    private val _selectedClassSchedule = MutableStateFlow<ClassSchedule?>(null)
    val selectedClassSchedule: StateFlow<ClassSchedule?> get() = _selectedClassSchedule

    private val _filteredClassSchedules = MutableStateFlow<List<ClassSchedule>>(emptyList())
    val filteredClassSchedules: StateFlow<List<ClassSchedule>> get() = _filteredClassSchedules

    private val _isLoading  = MutableStateFlow(false)
    val isLoading: MutableStateFlow<Boolean> get() = _isLoading

    var searchQuery = MutableStateFlow("")
        private set

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
                    if (_user.value?.id != null) {
                        getClassSchedules()
                    } else {
                        Log.e("TAG", "getUser: User is null")
                    }
                }
                is Resource.Failure -> {}
                Resource.Loading -> {}
            }
        }
    }

    private fun getClassSchedules() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileClassScheduleUseCases.getClassSchedulesByProfileId(_user.value?.id ?: "")
            when (result) {
                is Resource.Success -> {
                    Log.d("TAG", "getClassSchedules: ${result.data}")
                    _classSchedules.value = result.data
                    _filteredClassSchedules.value = result.data
                    _isLoading.value = false
                }
                is Resource.Failure -> {
                    Log.e("TAG", "getClassSchedules: ${result.message}")
                    _isLoading.value = false
                }
                Resource.Loading -> {}
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
        filterClassSchedules(query)
    }

    fun selectClassSchedule(classSchedule: ClassSchedule) {
        _selectedClassSchedule.value = classSchedule
    }

    fun filterClassSchedules(query: String) {
        if (query.isBlank()) {
            _filteredClassSchedules.value = _classSchedules.value
        } else {
            _filteredClassSchedules.value = _classSchedules.value.filter { classSchedule ->
                classSchedule.courseName.contains(query, ignoreCase = true)
            }
        }
    }

    fun resetClassSchedules() {
        searchQuery.value = ""
        _filteredClassSchedules.value = _classSchedules.value
    }
}