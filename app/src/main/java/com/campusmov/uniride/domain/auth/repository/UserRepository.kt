package com.campusmov.uniride.domain.auth.repository

import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.shared.util.Resource

interface UserRepository {
    suspend fun saveUserLocally(user: User): Resource<Unit>
    suspend fun getUserByEmailLocally(email: String): Resource<User>
    suspend fun getUserByIdLocally(id: String): Resource<User>
    suspend fun updateUserLocally(user: User): Resource<Unit>
    suspend fun getUserLocally(): Resource<User>
    suspend fun deleteAllUserLocally(): Resource<Unit>
}