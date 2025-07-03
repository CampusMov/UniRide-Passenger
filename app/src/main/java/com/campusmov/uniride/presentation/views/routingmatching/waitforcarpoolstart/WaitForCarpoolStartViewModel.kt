package com.campusmov.uniride.presentation.views.routingmatching.waitforcarpoolstart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.routingmatching.model.Carpool
import com.campusmov.uniride.domain.routingmatching.model.ECarpoolStatus
import com.campusmov.uniride.domain.routingmatching.usecases.CarpoolUseCases
import com.campusmov.uniride.domain.routingmatching.usecases.CarpoolWsUseCases
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaitForCarpoolStartViewModel @Inject constructor(
    private val carpoolUseCases: CarpoolUseCases,
    private val carpoolWsUseCases: CarpoolWsUseCases
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
                    connectToCarpoolWebSocket()
                }
                is Resource.Failure -> {
                    Log.e("TAG", "failed to fetch carpool: ${result.message}")
                    _isLoading.value = false
                }
                Resource.Loading -> {}
            }
        }
    }

    fun connectToCarpoolWebSocket(){
        viewModelScope.launch {
            var isConnected = false
            try {
                carpoolWsUseCases.connectCarpoolUseCase()
                isConnected = true
                Log.d("TAG", "WaitForCarpoolStartViewModel: Successfully connected to carpool WebSocket")
            } catch (e: Exception) {
                Log.e("TAG", "WaitForCarpoolStartViewModel: Error connecting to carpool WebSocket", e)
            } finally {
                if (isConnected) subscribeToCarpoolUpdates()
            }
        }
    }

    fun subscribeToCarpoolUpdates() {
        viewModelScope.launch {
           carpoolWsUseCases.subscribeCarpoolStatusUpdatesUseCase(currentCarpool.value!!.id)
               .collect { carpool ->
                   val status: String? = carpool.status.name
                   when(ECarpoolStatus.fromString(status)) {
                          ECarpoolStatus.IN_PROGRESS -> {
                            Log.d("TAG", "WaitForCarpoolStartViewModel: Carpool is now in progress: $carpool")
                            _currentCarpool.value = carpool
                              onCleared()
                          }
                          ECarpoolStatus.CANCELLED -> {
                            Log.d("TAG", "WaitForCarpoolStartViewModel: Carpool was cancelled: $carpool")
                            _currentCarpool.value = carpool
                              onCleared()
                          }
                          else -> {
                            Log.d("TAG", "WaitForCarpoolStartViewModel: Carpool status does not affect the current view: $carpool")
                          }
                   }
               }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("TAG", "WaitForCarpoolStartViewModel: ViewModel cleared, unsubscribing from WebSocket")
        viewModelScope.launch { carpoolWsUseCases.disconnectCarpoolUseCase() }
    }
}