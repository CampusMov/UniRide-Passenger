package com.campusmov.uniride.data.repository.auth

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
            userDao.insertUser(user.toEntity())
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun getUserByEmailLocally(email: String): Resource<User> = withContext(Dispatchers.IO) {
        try {
            val entity = userDao.getUserByEmail(email)
            if (entity == null) {
                Resource.Failure("User not found with email: $email")
            } else {
                Resource.Success(entity.toDomain())
            }
        } catch (e: IOException) {
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun getUserByIdLocally(id: String): Resource<User> = withContext(Dispatchers.IO) {
        try {
            val entity = userDao.getUserById(id)
            if (entity == null) {
                Resource.Failure("User not found with id: $id")
            } else {
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
            userDao.insertUser(user.toEntity())
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun getUserLocally(): Resource<User> = withContext(Dispatchers.IO) {
        try {
            val entity = userDao.getUser()
            if (entity == null) {
                Resource.Failure("The user is not logged in or does not exist.")
            } else {
                Resource.Success(entity.toDomain())
            }
        } catch (e: IOException) {
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun deleteAllUserLocally(): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            userDao.deleteAllUsers()
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Failure("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Resource.Failure("An unexpected error occurred: ${e.localizedMessage}")
        }
    }
}