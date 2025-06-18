package com.campusmov.uniride.presentation.views.profile.registerprofile

import android.os.Build
import androidx.annotation.RequiresApi
import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.domain.shared.model.EDay
import com.campusmov.uniride.domain.shared.model.Location
import java.time.LocalTime
import java.util.UUID

data class CurrentClassScheduleState(
    val isEditing: Boolean = false,
    val editingIndex: String? = null,
    val courseName: String = "",
    val selectedLocation: Location? = null,
    val startedAt: LocalTime? = null,
    val endedAt: LocalTime? = null,
    val selectedDay: EDay? = null
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDomain(): ClassSchedule {
        val loc = selectedLocation!!
        return ClassSchedule(
            id = editingIndex?: UUID.randomUUID().toString(),
            courseName   = courseName,
            locationName = loc.name,
            latitude     = loc.latitude,
            longitude    = loc.longitude,
            address      = loc.address,
            startedAt    = startedAt!!,
            endedAt      = endedAt!!,
            selectedDay  = selectedDay!!
        )
    }
}