package com.campusmov.uniride.presentation.views.routingmatching.searchcarpool

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchCarpoolViewModel @Inject constructor (
    // TODO: Add use case for searching carpool
): ViewModel() {
    private val _amountSeatsRequested = MutableStateFlow<Int>(1)
    val amountSeatsRequested: StateFlow<Int> get() = _amountSeatsRequested

    fun increaseAmountSeatsRequested() {
        if (_amountSeatsRequested.value < 4) {
            _amountSeatsRequested.value++
        }
    }

    fun decreaseAmountSeatsRequested() {
        if (_amountSeatsRequested.value > 1) {
            _amountSeatsRequested.value--
        }
    }
}