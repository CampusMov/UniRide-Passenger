package com.campusmov.uniride.domain.routingmatching.usecases

import com.campusmov.uniride.domain.routingmatching.repository.CarpoolRepository

class GetCarpoolByIdUseCase(private val carpoolRepository: CarpoolRepository) {
    suspend operator fun invoke(carpoolId: String) = carpoolRepository.searchCarpoolById(carpoolId)
}