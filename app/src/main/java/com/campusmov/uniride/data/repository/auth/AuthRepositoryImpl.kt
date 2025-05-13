package com.campusmov.uniride.data.repository.auth

import android.util.Log
import com.campusmov.uniride.data.datasource.remote.service.AuthService
import com.campusmov.uniride.domain.auth.model.AuthEmailVerificationResponse
import com.campusmov.uniride.domain.auth.repository.AuthRepository
import com.campusmov.uniride.domain.shared.util.Resource

class AuthRepositoryImpl(private val authService: AuthService): AuthRepository {
    override suspend fun verifyEmail(email: String): Resource<AuthEmailVerificationResponse> {
        return try {
            val response = authService.sendVerificationEmail(email)
            if (response.isSuccessful) {
                Log.d("AuthRepositoryImpl", "Email verification sent successfully")
                Resource.Success(response.body()!!)
            } else {
                Log.d("AuthRepositoryImpl", "Error: ${response.errorBody()}")
                Resource.Failure("Error al enviar el correo de verificación")
            }
        } catch (e: Exception) {
            Log.d("AuthRepositoryImpl", "Error: ${e.message}")
            e.printStackTrace()
            Resource.Failure(e.message?: "Error desconocido")
        }
    }
}