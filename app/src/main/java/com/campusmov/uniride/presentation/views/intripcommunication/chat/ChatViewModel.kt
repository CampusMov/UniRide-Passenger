package com.campusmov.uniride.presentation.views.intripcommunication.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.intripcommunication.model.Chat
import com.campusmov.uniride.domain.intripcommunication.model.Message
import com.campusmov.uniride.domain.intripcommunication.usecases.InTripCommunicationUseCases
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val inTripCommunicationUseCases: InTripCommunicationUseCases
) : ViewModel() {

    private val _chat    = MutableStateFlow<Chat?>(null)
    val chat            = _chat.asStateFlow()

    private val _history = MutableStateFlow<List<Message>>(emptyList())
    private val _live    = MutableStateFlow<List<Message>>(emptyList())

    val messages: StateFlow<List<Message>> = combine(_history, _live) { h, l ->
        (h + l).distinctBy { it.messageId }.sortedBy { it.sentAt }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var newMessageText = MutableStateFlow("")
        private set

    private val _isSending = MutableStateFlow(false)
    val isSending         = _isSending.asStateFlow()

    private val _error     = MutableSharedFlow<String>()
    val error             = _error.asSharedFlow()

    private var currentChatId: String? = null
    private var currentUserId: String? = null

    private var sessionJob: Job? = null
    private var subscribeJob: Job? = null

    fun openChat(passengerId: String, carpoolId: String) {
        currentUserId = passengerId
        _history.value = emptyList()
        _live.value    = emptyList()

        if (!isValidParamsToOpenChat(passengerId, carpoolId)) {
            Log.e("ChatViewModel", "Invalid parameters to open chat - passengerId: $passengerId, carpoolId: $carpoolId")
            return
        }

        viewModelScope.launch {
            when (val res = inTripCommunicationUseCases.getPassengerChat(passengerId, carpoolId)) {
                is Resource.Success -> {
                    Log.d("TAG", "Chat opened: ${res.data}")
                    _chat.value      = res.data
                    currentChatId    = res.data.chatId
                    startSession()
                }
                is Resource.Failure -> {
                    Log.e("TAG", "Failed to open chat: ${res.message}")
                    _error.emit(res.message)
                }
                else -> {}
            }
        }
    }

    private fun isValidParamsToOpenChat(passengerId: String, carpoolId: String): Boolean {
        return passengerId.isNotEmpty() && carpoolId.isNotEmpty()
    }

    private fun startSession() {
        sessionJob?.cancel()
        subscribeJob?.cancel()

        sessionJob = viewModelScope.launch {
            try {
                inTripCommunicationUseCases.connectToChat()

                loadHistory()

                subscribeToLiveMessages()

            } catch (e: Exception) {
                _error.emit("Error connecting to chat: ${e.localizedMessage}")
            }
        }
    }

    private suspend fun loadHistory() {
        val chatId = currentChatId ?: return

        when (val res = inTripCommunicationUseCases.getMessages(chatId)) {
            is Resource.Success -> {
                _history.value = res.data
                res.data
                    .filter { it.status != "READ" && it.senderId != currentUserId }
                    .forEach {
                        inTripCommunicationUseCases.markMessageAsRead(chatId, it.messageId, currentUserId!!)
                    }
            }
            is Resource.Failure -> _error.emit(res.message)
            else -> {}
        }
    }

    private fun subscribeToLiveMessages() {
        val chatId = currentChatId ?: return

        subscribeJob = viewModelScope.launch {
            inTripCommunicationUseCases.observeLiveMessages(chatId)
                .catch { _error.emit("In live error: ${it.localizedMessage}") }
                .collect { msg ->
                    _live.update { currentList ->
                        val newList = currentList.toMutableList()
                        if (msg.senderId == currentUserId) {
                            newList.removeAll { it.status == "SENDING" }
                            newList.add(msg)
                        } else {
                            newList.add(msg)
                            currentUserId?.let { userId ->
                                inTripCommunicationUseCases.markMessageAsRead(chatId, msg.messageId, userId)
                            }
                        }
                        newList
                    }
                }
        }
    }

    fun sendMessage() {
        val chatId = currentChatId ?: return
        val userId = currentUserId ?: return
        val text = newMessageText.value.trim().takeIf { it.isNotEmpty() } ?: return
        val provisionalId = "prov_${UUID.randomUUID()}"

        viewModelScope.launch {
            when (inTripCommunicationUseCases.sendMessage(chatId, userId, text)) {
                is Resource.Success -> {
                    _live.update { currentList ->
                        currentList.map {
                            if (it.messageId == provisionalId) it.copy(status = "SENT") else it
                        }
                    }
                    newMessageText.value = ""
                }
                is Resource.Failure -> {
                    _error.emit("Could not send message")
                    _live.update { currentList ->
                        currentList.map {
                            if (it.messageId == provisionalId) it.copy(status = "FAILED") else it
                        }
                    }
                }
                else -> {
                    _error.emit("An unexpected error occurred while sending the message")
                    _live.update { currentList ->
                        currentList.map {
                            if (it.messageId == provisionalId) it.copy(status = "FAILED") else it
                        }
                    }
                }
            }
        }
    }

    fun closeChatSession() {
        sessionJob?.cancel()
        subscribeJob?.cancel()
        currentChatId = null
        _chat.value   = null
    }
}