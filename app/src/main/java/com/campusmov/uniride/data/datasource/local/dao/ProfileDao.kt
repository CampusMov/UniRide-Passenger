package com.campusmov.uniride.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.campusmov.uniride.data.datasource.local.entities.ProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: ProfileEntity)

    @Query("SELECT * FROM profiles WHERE userId = :userId")
    suspend fun getProfileById(userId: String): ProfileEntity?

    @Query("DELETE FROM profiles")
    suspend fun deleteAllProfiles()

    @Query("SELECT localChanges FROM profiles WHERE userId = :userId")
    suspend fun hasLocalChanges(userId: String): Int

    @Query("UPDATE profiles SET localChanges = :value WHERE userId = :userId")
    suspend fun updateLocalChanges(value: Int, userId: String)
}