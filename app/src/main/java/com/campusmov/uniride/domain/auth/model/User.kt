package com.campusmov.uniride.domain.auth.model

import com.campusmov.uniride.data.datasource.local.entities.UserEntity

data class User (
    val id: String = "1",
    val email: String = "",
    val status: UserStatus = UserStatus.NOT_VERIFIED,
    val roles: List<Role> = listOf(Role.PASSENGER)
)

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        status = status.name
    )
}