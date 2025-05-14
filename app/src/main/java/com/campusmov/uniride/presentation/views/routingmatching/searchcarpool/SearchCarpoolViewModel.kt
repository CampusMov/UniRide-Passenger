package com.campusmov.uniride.presentation.views.routingmatching.searchcarpool

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchCarpoolViewModel @Inject constructor (
    // TODO: Add use case for searching carpool
): ViewModel() {
    var isSelectedPickUpPoint = mutableStateOf(false)
    var isSelectedScheduleClass = mutableStateOf(false)
}