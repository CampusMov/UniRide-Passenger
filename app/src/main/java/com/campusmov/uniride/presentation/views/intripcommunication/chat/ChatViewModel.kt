package com.campusmov.uniride.presentation.views.intripcommunication.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusmov.uniride.domain.intripcommunication.model.Chat
import com.campusmov.uniride.domain.intripcommunication.model.Message
import com.campusmov.uniride.domain.intripcommunication.usecases.InTripCommunicationUseCases
import com.campusmov.uniride.domain.shared.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val uc: InTripCommunicationUseCases
) : ViewModel() {

    private val _chat       = MutableStateFlow<Chat?>(null)
    val chat: StateFlow<Chat?> = _chat

    private val _history    = MutableStateFlow<List<Message>>(emptyList())
    private val _live       = MutableStateFlow<List<Message>>(emptyList())

    val messages: StateFlow<List<Message>> = combine(_history, _live) { h, l ->
        (h + l).sortedBy { it.sentAt }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var newMessageText = MutableStateFlow("")
    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending

    private val _error    = MutableSharedFlow<String>()

    private var currentChatId: String? = null
    private var currentUserId: String? = null

    init {
        uc.observeLiveMessages()
            .onEach { msg ->
                _live.update { it + msg }
                if (msg.status != "READ" && msg.senderId != currentUserId) {
                    currentChatId?.let { chatId ->
                        uc.markMessageAsRead(chatId, msg.messageId, currentUserId!!)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun openChat(passengerId: String, carpoolId: String) {
        currentUserId = passengerId
        viewModelScope.launch {
            uc.getPassengerChat(passengerId, carpoolId).let {
                if (it is Resource.Success) _chat.value = it.data
            }
        }
    }

    fun startSessionWhenReady() {
        val chatId = _chat.value?.chatId ?: return
        currentChatId = chatId
        viewModelScope.launch {
            uc.getMessages(chatId).let {
                if (it is Resource.Success) {
                    _history.value = it.data
                    it.data.filter { m -> m.status != "READ" && m.senderId != currentUserId }
                        .forEach { m -> uc.markMessageAsRead(chatId, m.messageId, currentUserId!!) }
                }
            }
            uc.connectToChat(chatId)
        }
    }

    fun sendMessage() {
        val chatId = currentChatId ?: return
        val userId = currentUserId ?: return
        val content = newMessageText.value.trim().takeIf { it.isNotEmpty() } ?: return

        _isSending.value = true
        viewModelScope.launch {
            when (val res = uc.sendMessage(chatId, userId, content)) {
                is Resource.Success -> {
                    newMessageText.value = ""
                }
                is Resource.Failure -> {
                    _error.emit(res.message)
                }
                else -> {}
            }
            _isSending.value = false
        }
    }

    fun closeSession() {
        currentChatId = null
        uc.disconnectChat()
    }
}