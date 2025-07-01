package com.campusmov.uniride.domain.intripcommunication.request

import com.google.gson.annotations.SerializedName

data class CreateChatRequest(
    @SerializedName("carpoolId")   val carpoolId: String,
    @SerializedName("driverId")    val driverId: String,
    @SerializedName("passengerId") val passengerId: String
)