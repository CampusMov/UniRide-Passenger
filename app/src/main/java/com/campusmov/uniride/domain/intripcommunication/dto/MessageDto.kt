package com.campusmov.uniride.domain.intripcommunication.dto

import com.campusmov.uniride.domain.intripcommunication.model.Message
import com.google.gson.annotations.SerializedName

data class MessageDto(
    @SerializedName("messageId") val messageId: String,
    @SerializedName("senderId") val senderId: String,
    @SerializedName("content") val content: String,
    @SerializedName("sentAt") val sentAt: String,
    @SerializedName("status") val status: String
)

fun MessageDto.toDomain() = Message(
    messageId = messageId,
    senderId = senderId,
    content = content,
    sentAt = sentAt,
    status = status
)
