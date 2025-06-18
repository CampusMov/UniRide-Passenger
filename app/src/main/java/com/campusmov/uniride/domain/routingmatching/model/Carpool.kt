package com.campusmov.uniride.domain.routingmatching.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.campusmov.uniride.domain.shared.model.EDay
import com.campusmov.uniride.domain.shared.model.Location
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class Carpool (
    val id: String = "1",
    val driverId: String = "",
    val vehicleId: String = "",
    val status: ECarpoolStatus = ECarpoolStatus.CREATED,
    val availableSeats: Int = 0,
    val maxPassengers: Int = 0,
    val scheduleId: String = "",
    val radius: Int = 0,
    val origin: Location = Location(),
    val destination: Location = Location(),
    val startedClassTime: LocalDateTime = LocalDateTime.now(),
    val endedClassTime: LocalDateTime = LocalDateTime.now(),
    val classDay: EDay = EDay.MONDAY,
    val isVisible: Boolean = true,
)
