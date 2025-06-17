package com.campusmov.uniride.presentation.views.routingmatching.carpoolssearchresults

import androidx.lifecycle.ViewModel
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.usecases.ProfileUseCases
import com.campusmov.uniride.domain.routingmatching.usecases.CarpoolUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CarpoolsSearchResultsViewModel @Inject constructor(
    private val carpoolUseCases: CarpoolUseCases,
    private val profileUseCases: ProfileUseCases
): ViewModel() {

    fun getProfileById(profileId: String): Profile {
        return TODO("Provide the return value")
    }
}