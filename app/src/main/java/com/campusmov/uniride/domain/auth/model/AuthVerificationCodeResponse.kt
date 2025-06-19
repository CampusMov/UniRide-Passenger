package com.campusmov.uniride.domain.auth.model

import com.google.gson.Gson

data class AuthVerificationCodeResponse (
    val id: String? = null,
    val email: String? = null,
    val status: UserStatus? = null,
    val roles: List<String>? = null,
){
    fun toJson(): String = Gson().toJson(this)

    companion object{
        fun fromJson(data: String): AuthVerificationCodeResponse = Gson().fromJson(data, AuthVerificationCodeResponse::class.java)
    }

}

fun AuthVerificationCodeResponse.toDomain(): User {
    return User(
        id = id ?: "",
        email = email ?: "",
        status = status ?: UserStatus.NOT_VERIFIED,
        roles = roles?.map { Role.fromString(it) } ?: listOf(Role.PASSENGER)
    )
}