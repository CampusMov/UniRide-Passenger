package com.campusmov.uniride.domain.intripcommunication.request

data class MarkMessageReadRequest(
    val readerId: String,
    val readAt: String
)