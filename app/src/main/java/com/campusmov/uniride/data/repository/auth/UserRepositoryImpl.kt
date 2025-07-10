package com.campusmov.uniride.data.repository.auth

import android.util.Log
import com.campusmov.uniride.data.datasource.local.dao.UserDao
import com.campusmov.uniride.data.datasource.local.entities.toDomain
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.model.toEntity
import com.campusmov.uniride.domain.auth.repository.UserRepository
import com.campusmov.uniride.domain.shared.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class UserRepositoryImpl (private val userDao: UserDao): UserRepository {
    override suspend fun saveUserLocally(user: User): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            Log.d("TAG", "UserRepositoryImpl: Saving user locally: $user")
            userDao.insertUser(user.toEntity())
            Resource.Success(Unit)
        } catch (e: IOException) {
            Log.e("UserRepositoryImpl", "Error saving user locally: ${e.localizedMessage}")
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "An unexpected error occurred: ${e.localizedMessage}")
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun getUserByEmailLocally(email: String): Resource<User> = withContext(Dispatchers.IO) {
        try {
            val entity = userDao.getUserByEmail(email)
            if (entity == null) {
                Log.e("UserRepositoryImpl", "User not found with email: $email")
                Resource.Failure("User not found with email: $email")
            } else {
                Log.d("UserRepositoryImpl", "User found with email: $email, User: $entity")
                Resource.Success(entity.toDomain())
            }
        } catch (e: IOException) {
            Log.e("UserRepositoryImpl", "Network error: ${e.localizedMessage}")
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "An unexpected error occurred: ${e.localizedMessage}")
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun getUserByIdLocally(id: String): Resource<User> = withContext(Dispatchers.IO) {
        try {
            val entity = userDao.getUserById(id)
            if (entity == null) {
                Log.e("UserRepositoryImpl", "User not found with id: $id")
                Resource.Failure("User not found with id: $id")
            } else {
                Log.d("UserRepositoryImpl", "User found with id: $id, User: $entity")
                Resource.Success(entity.toDomain())
            }
        } catch (e: IOException) {
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun updateUserLocally(user: User): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            Log.d("UserRepositoryImpl", "Updating user locally: $user")
            userDao.insertUser(user.toEntity())
            Resource.Success(Unit)
        } catch (e: IOException) {
            Log.e("UserRepositoryImpl", "Network error: ${e.localizedMessage}")
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "An unexpected error occurred: ${e.localizedMessage}")
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun getUserLocally(): Resource<User> = withContext(Dispatchers.IO) {
        try {
            val entity = userDao.getUser()
            if (entity == null) {
                Log.e("UserRepositoryImpl", "The user is not logged in or does not exist.")
                Resource.Failure("The user is not logged in or does not exist.")
            } else {
                Log.d("UserRepositoryImpl", "User found locally: $entity")
                Resource.Success(entity.toDomain())
            }
        } catch (e: IOException) {
            Log.e("UserRepositoryImpl", "Network error: ${e.localizedMessage}")
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "An unexpected error occurred: ${e.localizedMessage}")
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun deleteAllUserLocally(): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            userDao.deleteAllUsers()
            Log.d("UserRepositoryImpl", "All users deleted successfully.")
            Resource.Success(Unit)
        } catch (e: IOException) {
            Log.e("UserRepositoryImpl", "Network error: ${e.localizedMessage}")
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "An unexpected error occurred: ${e.localizedMessage}")
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }
}