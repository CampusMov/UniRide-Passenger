package com.campusmov.uniride.data.repository.auth

import android.util.Log
import com.campusmov.uniride.data.remote.auth.AuthService
import com.campusmov.uniride.data.remote.auth.LoginUserDto

class AuthRepository(private val authService: AuthService) {
    suspend fun sendVerificationEmail(email: String, roleName: List<String>) {
        val userDto = LoginUserDto(email, roleName)
        Log.d("DTO", "Enviando verificación a: ${userDto.email} con roles: ${userDto.roleName}")
        authService.sendVerificationEmail(userDto)
    }
}