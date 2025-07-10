package com.campusmov.uniride.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.campusmov.uniride.data.datasource.local.assembler.ClassScheduleConverter
import com.campusmov.uniride.data.datasource.local.dao.ProfileDao
import com.campusmov.uniride.data.datasource.local.dao.UserDao
import com.campusmov.uniride.data.datasource.local.entities.ProfileEntity
import com.campusmov.uniride.data.datasource.local.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ProfileEntity::class
   ],
    version = 1,
    exportSchema = false
)
@TypeConverters(ClassScheduleConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun profileDao(): ProfileDao
}