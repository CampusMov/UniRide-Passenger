package com.campusmov.uniride.data.datasource.remote.dto

import com.campusmov.uniride.domain.auth.model.Role
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.model.UserStatus

data class UserResponseDto(
    val id: String? = null,
    val email: String? = null,
    val status: String? = null,
    val roles: List<String>? = null
) {
    fun toDomain(): User{
        return User(
            id = id ?: "",
            email = email ?: "",
            status = UserStatus.fromString(status ?: UserStatus.NOT_VERIFIED.name),
            roles = roles?.map { Role.fromString(it) } ?: listOf(Role.PASSENGER)
        )
    }
}