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