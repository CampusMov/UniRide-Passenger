package com.campusmov.uniride.domain.intripcommunication.model

data class Chat(
    val chatId: String,
    val carpoolId: String,
    val driverId: String,
    val passengerId: String,
    val createdAt: String,
    val lastMessageAt: String,
    val status: String,
    val lastMessagePreview: String,
    val unreadCount: Int
)
