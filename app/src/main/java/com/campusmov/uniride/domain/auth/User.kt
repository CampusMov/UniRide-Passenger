package com.campusmov.uniride.domain.auth

data class User (
    val id: String,
    val email: String,
    val status: UserStatus,
    val roles: List<Role>
)