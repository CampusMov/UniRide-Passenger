package com.campusmov.uniride.data.datasource.local.assembler

import androidx.room.TypeConverter
import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ClassScheduleConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromClassScheduleList(classSchedules: List<ClassSchedule>?): String? {
        return gson.toJson(classSchedules)
    }

    @TypeConverter
    fun toClassScheduleList(classSchedulesString: String?): List<ClassSchedule>? {
        if (classSchedulesString.isNullOrEmpty()) {
            return null
        }
        val listType = object : TypeToken<List<ClassSchedule>>() {}.type
        return gson.fromJson(classSchedulesString, listType)
    }
}