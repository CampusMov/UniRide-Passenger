package com.campusmov.uniride.presentation.views.profile.registerprofile

import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.domain.shared.model.EDay
import com.campusmov.uniride.domain.shared.model.Location
import java.time.LocalDateTime
import java.util.UUID

data class ClassScheduleDialogState(
    val isEditing: Boolean = false,
    val editingIndex: Int? = null,
    val courseName: String = "",
    val selectedLocation: Location? = null,
    val startedAt: LocalDateTime? = null,
    val endedAt: LocalDateTime? = null,
    val selectedDay: EDay? = null
) {
    fun toDomain(): ClassSchedule {
        val loc = selectedLocation!!
        return ClassSchedule(
            id = UUID.randomUUID().toString(),
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