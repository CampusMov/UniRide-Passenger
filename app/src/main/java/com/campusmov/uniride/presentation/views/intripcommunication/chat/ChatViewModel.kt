package com.campusmov.uniride.presentation.views.intripcommunication.chat

import android.os.Build
import androidx.annotation.RequiresApi
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
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
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
    private var connectJob: Job? = null
    private var historyJob: Job? = null

    init {
        viewModelScope.launch {
            inTripCommunicationUseCases.observeLiveMessages()
                .catch { _error.emit("Live error: ${it.localizedMessage}") }
                .collect { msg ->
                    _live.update { cur ->
                        val list = cur.toMutableList()
                        val idx = list.indexOfFirst { it.messageId == msg.messageId }
                        if (idx >= 0) list[idx] = msg else list.add(msg)
                        list
                    }
                    if (msg.status != "READ" && msg.senderId != currentUserId) {
                        currentChatId?.let {
                            inTripCommunicationUseCases.markMessageAsRead(it, msg.messageId, currentUserId!!)
                        }
                    }
                }
        }
    }

    fun openChat(passengerId: String, carpoolId: String) {
        currentUserId = passengerId
        _history.value = emptyList()
        _live.value    = emptyList()

        viewModelScope.launch {
            when (val res = inTripCommunicationUseCases.getPassengerChat(passengerId, carpoolId)) {
                is Resource.Success -> {
                    _chat.value      = res.data
                    currentChatId    = res.data.chatId
                    startSession(res.data.chatId)
                }
                is Resource.Failure -> _error.emit(res.message)
                else -> {}
            }
        }
    }

    private fun startSession(chatId: String) {
        connectJob?.cancel()
        connectJob = viewModelScope.launch {
            inTripCommunicationUseCases.connectToChat(chatId)
            loadHistory(chatId)
        }
    }

    private fun loadHistory(chatId: String) {
        historyJob?.cancel()
        historyJob = viewModelScope.launch {
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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage() {
        val chatId = currentChatId ?: return
        val userId = currentUserId ?: return
        val text   = newMessageText.value.trim().takeIf { it.isNotEmpty() } ?: return

        val provisionalId = "prov_${UUID.randomUUID()}"
        val sentAt        = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME)
        val provisional   = Message(provisionalId, userId, text, sentAt, status = "SENDING")

        _live.update { it + provisional }
        newMessageText.value = ""
        _isSending.value = true

        viewModelScope.launch {
            when (val res = inTripCommunicationUseCases.sendMessage(chatId, userId, text)) {
                is Resource.Success -> res.data.let { confirmed ->
                    _live.update { cur ->
                        val list = cur.toMutableList()
                        val idx = list.indexOfFirst { it.messageId == provisionalId }
                        if (idx >= 0) list[idx] = confirmed
                        else if (list.none { it.messageId == confirmed.messageId }) list.add(confirmed)
                        list
                    }
                }
                is Resource.Failure -> {
                    _error.emit(res.message)
                    _live.update { cur ->
                        cur.map {
                            if (it.messageId == provisionalId) it.copy(status = "FAILED") else it
                        }
                    }
                }
                else -> {}
            }
            _isSending.value = false
        }
    }

    fun closeChatSession() {
        connectJob?.cancel()
        currentChatId = null
        _chat.value   = null
    }
}