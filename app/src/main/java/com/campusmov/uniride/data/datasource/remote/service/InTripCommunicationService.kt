package com.campusmov.uniride.data.datasource.remote.service

import com.campusmov.uniride.domain.intripcommunication.dto.ChatDto
import com.campusmov.uniride.domain.intripcommunication.request.CreateChatRequest
import com.campusmov.uniride.domain.intripcommunication.dto.MessageDto
import com.campusmov.uniride.domain.intripcommunication.request.MarkMessageReadRequest
import com.campusmov.uniride.domain.intripcommunication.request.SendMessageRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface InTripCommunicationService {
    @POST("http://10.0.2.2:9300/chats")
    suspend fun createChat(
        @Body request: CreateChatRequest
    ): Response<ChatDto>

    @POST("chats/{chatId}/close")
    suspend fun closeChat(
        @Path("chatId") chatId: String
    ): Response<Unit>

    @GET("chats/passenger/{passengerId}/ride/{carpoolId}")
    suspend fun getPassengerChat(
        @Path("passengerId") passengerId: String,
        @Path("carpoolId") carpoolId: String
    ): Response<ChatDto>

    @GET("chats/driver/{driverId}")
    suspend fun getDriverChats(
        @Path("driverId") driverId: String
    ): Response<List<ChatDto>>

    @GET("chats/{chatId}/messages")
    suspend fun getMessages(
        @Path("chatId") chatId: String
    ): Response<List<MessageDto>>

    @POST("chats/{chatId}/messages")
    suspend fun sendMessage(
        @Path("chatId") chatId: String,
        @Body request: SendMessageRequest
    ): Response<MessageDto>

    @POST("chats/{chatId}/messages/{messageId}/read")
    suspend fun markMessageAsRead(
        @Path("chatId") chatId: String,
        @Path("messageId") messageId: String,
        @Body request: MarkMessageReadRequest
    ): Response<Unit>
}