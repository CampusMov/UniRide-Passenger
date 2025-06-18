package com.campusmov.uniride.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.toIsoString(): String = this.format(DateTimeFormatter.ofPattern("HH:mm:ss"))