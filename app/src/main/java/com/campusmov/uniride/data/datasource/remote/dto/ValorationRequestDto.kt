package com.campusmov.uniride.data.datasource.remote.dto


data class ValorationRequestDto(
    val userId: String?,
    val senderId: String?,
    val reputationScore: Double?,
    val message: String?,
) {
}