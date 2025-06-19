package com.campusmov.uniride.data.repository.auth

import android.util.Log
import com.campusmov.uniride.data.datasource.remote.service.AuthService
import com.campusmov.uniride.domain.auth.model.AuthVerificationCodeResponse
import com.campusmov.uniride.domain.auth.repository.AuthRepository
import com.campusmov.uniride.domain.shared.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(private val authService: AuthService): AuthRepository {
    override suspend fun verifyEmail(email: String): Resource<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = authService.sendVerificationEmail(email)
            if (response.isSuccessful) {
                Log.d("AuthRepositoryImpl",  "Correo enviado correctamente a: $email")
                Resource.Success(response.body()!!)
            } else {
                Log.d("AuthRepositoryImpl", "Error: ${response.errorBody()?.string()}")
                Resource.Failure("Error al enviar el correo de verificación")
            }
        } catch (e: Exception) {
            Log.d("AuthRepositoryImpl", "Error: ${e.message}")
            Resource.Failure(e.message?: "Error desconocido")
        }
    }

    override suspend fun verifyCode(code: String, email: String, role: String): Resource<AuthVerificationCodeResponse>  = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = authService.sendVerificationCode(email, code, role)
            if (response.isSuccessful) {
                Log.d("AuthRepositoryImpl", "Código verificado correctamente")
                Resource.Success(response.body()!!)
            } else {
                Log.d("AuthRepositoryImpl", "Error: ${response.errorBody()?.string()}")
                Resource.Failure("Error al verificar el código")
            }
        } catch (e: Exception) {
            Log.d("AuthRepositoryImpl", "Error: ${e.message}")
            e.printStackTrace()
            Resource.Failure(e.message ?: "Error desconocido")
        }
    }
}