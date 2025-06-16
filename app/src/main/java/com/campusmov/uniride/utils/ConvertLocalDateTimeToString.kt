package com.campusmov.uniride.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toIsoString(): String = this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))