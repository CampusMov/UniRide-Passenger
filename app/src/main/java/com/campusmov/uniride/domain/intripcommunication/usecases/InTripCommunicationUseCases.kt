package com.campusmov.uniride.domain.intripcommunication.usecases

data class InTripCommunicationUseCases(
    val createChat: CreateChatUseCase,
    val closeChat: CloseChatUseCase,
    val getPassengerChat: GetPassengerChatUseCase,
    val getMessages: GetMessagesUseCase,
    val sendMessage: SendMessageUseCase,
    val markMessageAsRead: MarkMessageAsReadUseCase,
    val connectToChat: ConnectToChatUseCase,
    val disconnectChat: DisconnectChatUseCase,
    val observeLiveMessages: ObserveLiveMessagesUseCase

    )
