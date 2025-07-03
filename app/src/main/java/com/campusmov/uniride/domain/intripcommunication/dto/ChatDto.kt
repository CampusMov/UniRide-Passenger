package com.campusmov.uniride.domain.intripcommunication.dto

import com.campusmov.uniride.domain.intripcommunication.model.Chat
import com.google.gson.annotations.SerializedName

data class ChatDto(
    @SerializedName("chatId") val chatId: String,
    @SerializedName("carpoolId") val carpoolId: String,
    @SerializedName("driverId") val driverId: String,
    @SerializedName("passengerId") val passengerId: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("lastMessageAt") val lastMessageAt: String,
    @SerializedName("status") val status: String,
    @SerializedName("lastMessagePreview") val lastMessagePreview: String,
    @SerializedName("unreadCount") val unreadCount: Int
)

fun ChatDto.toDomain() = Chat(
    chatId            = chatId,
    carpoolId         = carpoolId,
    driverId          = driverId,
    passengerId       = passengerId,
    createdAt         = createdAt,
    lastMessageAt     = lastMessageAt,
    status            = status,
    lastMessagePreview= lastMessagePreview,
    unreadCount       = unreadCount
)
