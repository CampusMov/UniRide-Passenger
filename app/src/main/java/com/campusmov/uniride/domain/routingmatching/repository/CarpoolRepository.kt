package com.campusmov.uniride.domain.routingmatching.repository

import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.domain.routingmatching.model.Carpool
import com.campusmov.uniride.domain.shared.model.Location
import com.campusmov.uniride.domain.shared.util.Resource

interface CarpoolRepository {
    suspend fun searchAvailableCarpools(location: Location, classSchedule: ClassSchedule, requestedSeats: Int): Resource<List<Carpool>>
}