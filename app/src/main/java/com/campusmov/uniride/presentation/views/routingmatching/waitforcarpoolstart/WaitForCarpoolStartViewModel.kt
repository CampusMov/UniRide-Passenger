package com.campusmov.uniride.presentation.views.routingmatching.waitforcarpoolstart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.routingmatching.model.Carpool
import com.campusmov.uniride.domain.routingmatching.usecases.CarpoolUseCases
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaitForCarpoolStartViewModel @Inject constructor(
    private val carpoolUseCases: CarpoolUseCases
): ViewModel(){
    private val _currentCarpool = MutableStateFlow<Carpool?>(null)
    val currentCarpool: StateFlow<Carpool?> get() = _currentCarpool

    private val _isLoading  = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun getCarpoolById(carpoolId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = carpoolUseCases.getCarpoolById(carpoolId)
            when (result) {
                is Resource.Success -> {
                    Log.d("TAG", "successfully fetched carpool: ${result.data}")
                    _isLoading.value = false
                    _currentCarpool.value = result.data
                }
                is Resource.Failure -> {
                    Log.e("TAG", "failed to fetch carpool: ${result.message}")
                    _isLoading.value = false
                }
                Resource.Loading -> {}
            }
        }
    }
}