package com.campusmov.uniride.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.campusmov.uniride.data.datasource.local.dao.UserDao
import com.campusmov.uniride.data.datasource.local.entities.UserEntity

@Database(
    entities = [
        UserEntity::class
   ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}