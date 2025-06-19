package com.campusmov.uniride.domain.routingmatching.usecases

import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.domain.routingmatching.repository.CarpoolRepository
import com.campusmov.uniride.domain.shared.model.Location

class SearchCarpoolsAvailableUseCase(private val carpoolRepository: CarpoolRepository) {
    suspend operator fun invoke(location: Location, classSchedule: ClassSchedule, requestedSeats: Int) = carpoolRepository.searchAvailableCarpools(location, classSchedule, requestedSeats)
}