package com.campusmov.uniride.data.repository.intripcommunication

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.campusmov.uniride.data.datasource.remote.service.InTripCommunicationService
import com.campusmov.uniride.domain.intripcommunication.model.Chat
import com.campusmov.uniride.domain.intripcommunication.model.Message
import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationRepository
import com.campusmov.uniride.domain.intripcommunication.request.CreateChatRequest
import com.campusmov.uniride.domain.intripcommunication.request.SendMessageRequest
import com.campusmov.uniride.domain.intripcommunication.dto.ChatDto
import com.campusmov.uniride.domain.intripcommunication.dto.MessageDto
import com.campusmov.uniride.domain.intripcommunication.dto.toDomain
import com.campusmov.uniride.domain.intripcommunication.request.MarkMessageReadRequest
import com.campusmov.uniride.domain.shared.util.Resource
import okhttp3.internal.userAgent
import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class InTripCommunicationRepositoryImpl @Inject constructor(
    private val service: InTripCommunicationService
) : InTripCommunicationRepository {

    override suspend fun createChat(
        carpoolId: String,
        driverId: String,
        passengerId: String
    ): Resource<Chat> = try {
        val response = service.createChat(
            CreateChatRequest(
                carpoolId = carpoolId,
                driverId = driverId,
                passengerId = passengerId
            )
        )
        if (response.isSuccessful) {
            val dto = response.body()!!
            val chat = dto.toDomain()
            Log.d("InTripCommRepo", "Chat created: ${chat.chatId}")
            Resource.Success(chat)
        } else {
            val err = response.errorBody()?.string().orEmpty()
            Log.e("InTripCommRepo", "Failed to create chat: $err")
            Resource.Failure("Failed to create chat")
        }
    } catch (e: Exception) {
        Log.e("InTripCommRepo", "Error creating chat", e)
        Resource.Failure(e.message.orEmpty())
    }

    override suspend fun closeChat(chatId: String): Resource<Unit> = try {
        val response = service.closeChat(chatId)
        if (response.isSuccessful) {
            Log.d("InTripCommRepo", "Chat closed: $chatId")
            Resource.Success(Unit)
        } else {
            val err = response.errorBody()?.string().orEmpty()
            Log.e("InTripCommRepo", "Failed to close chat: $err")
            Resource.Failure("Failed to close chat")
        }
    } catch (e: Exception) {
        Log.e("InTripCommRepo", "Error closing chat", e)
        Resource.Failure(e.message.orEmpty())
    }

    override suspend fun getPassengerChat(
        passengerId: String,
        carpoolId: String
    ): Resource<Chat> = try {
        val response = service.getPassengerChat(passengerId, carpoolId)
        if (response.isSuccessful) {
            val dto: ChatDto = response.body()!!
            Resource.Success(dto.toDomain())
        } else {
            val err = response.errorBody()?.string().orEmpty()
            Log.e("InTripCommRepo", "Failed to fetch passenger chat: $err")
            Resource.Failure("Failed to fetch passenger chat")
        }
    } catch (e: Exception) {
        Log.e("InTripCommRepo", "Error fetching passenger chat", e)
        Resource.Failure(e.message.orEmpty())
    }

    override suspend fun getMessages(chatId: String): Resource<List<Message>> = try {
        val response = service.getMessages(chatId)
        if (response.isSuccessful) {
            val listDto: List<MessageDto> = response.body()!!
            Resource.Success(listDto.map { it.toDomain() })
        } else {
            val err = response.errorBody()?.string().orEmpty()
            Log.e("InTripCommRepo", "Failed to fetch messages: $err")
            Resource.Failure("Failed to fetch messages")
        }
    } catch (e: Exception) {
        Log.e("InTripCommRepo", "Error fetching messages", e)
        Resource.Failure(e.message.orEmpty())
    }

    override suspend fun sendMessage(
        chatId: String,
        senderId: String,
        content: String
    ): Resource<Message> = try {
        val response = service.sendMessage(
            chatId,
            SendMessageRequest(
                senderId = senderId,
                content = content
            )
        )
        if (response.isSuccessful) {
            val dto = response.body()!!
            Resource.Success(dto.toDomain())
        } else {
            val err = response.errorBody()?.string().orEmpty()
            Log.e("InTripCommRepo", "Failed to send message: $err")
            Resource.Failure("Failed to send message")
        }
    } catch (e: Exception) {
        Log.e("InTripCommRepo", "Error sending message", e)
        Resource.Failure(e.message.orEmpty())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun markMessageAsRead(
        chatId: String,
        messageId: String,
        readerId: String
    ): Resource<Unit> = try {
        val nowIso = OffsetDateTime.now(ZoneOffset.UTC).toString()
        val response = service.markMessageAsRead(
            chatId,
            messageId,
            MarkMessageReadRequest(
                readerId = readerId,
                readAt   = nowIso
            )
        )
        if (response.isSuccessful) {
            Resource.Success(Unit)
        } else {
            Resource.Failure("Failed to mark message as read")
        }
    } catch (e: Exception) {
        Resource.Failure(e.message.orEmpty())
    }
}
