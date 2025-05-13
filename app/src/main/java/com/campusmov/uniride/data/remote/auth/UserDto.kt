package com.campusmov.uniride.data.remote.auth

import com.campusmov.uniride.domain.auth.Role
import com.campusmov.uniride.domain.auth.User
import com.campusmov.uniride.domain.auth.UserStatus

data class UserDto(
    val id: String,
    val email: String,
    val status: String,
    val roles: List<Role>

)

fun UserDto.toUser() = User(
    id = id,
    email = email,
    status = UserStatus.valueOf(status),
    roles = roles
)