package com.campusmov.uniride.domain.intripcommunication.model

data class Message(
    val messageId: String,
    val senderId: String,
    val content: String,
    val sentAt: String,
    val status: String
)