package com.campusmov.uniride.domain.profile.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.campusmov.uniride.domain.shared.model.EDay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class ClassSchedule(
    val id: String = "1",
    val courseName: String,
    val locationName: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String,
    val startedAt: LocalTime,
    val endedAt: LocalTime,
    val selectedDay: EDay
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleTime(): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return "${selectedDay.showDay()} - ${startedAt.format(formatter)} a ${endedAt.format(formatter)}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun timeRange(): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return "${startedAt.format(formatter)} - ${endedAt.format(formatter)}"
    }
}