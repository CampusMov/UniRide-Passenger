package com.campusmov.uniride.domain.intripcommunication.request

import com.google.gson.annotations.SerializedName

data class SendMessageRequest(
    @SerializedName("senderId") val senderId: String,
    @SerializedName("content")  val content: String
)