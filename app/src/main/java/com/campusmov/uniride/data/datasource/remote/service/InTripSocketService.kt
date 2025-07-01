package com.campusmov.uniride.data.datasource.remote.service

import android.util.Log
import com.campusmov.uniride.domain.intripcommunication.dto.MessageDto
import com.campusmov.uniride.domain.intripcommunication.dto.toDomain
import com.campusmov.uniride.domain.intripcommunication.model.Message
import com.google.gson.Gson
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import javax.inject.Inject
import javax.inject.Named

@ViewModelScoped
class InTripSocketService @Inject constructor(
    @Named("socketUrl") private val baseUrl: String,
    private val gson: Gson
) {
    private val _incoming = MutableSharedFlow<Message>()
    val incoming: SharedFlow<Message> = _incoming

    private var stompClient: StompClient? = null
    private val compositeDisposable = CompositeDisposable()

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var currentChatId: String? = null

    companion object {
        private const val TAG = "InTripSocket"
    }

    fun connect(chatId: String) {
        if (stompClient?.isConnected == true && currentChatId == chatId) {
            Log.d(TAG, "Already connected to chat: $chatId")
            return
        }

        disconnect()

        currentChatId = chatId
        val okHttpClient = OkHttpClient.Builder().build()

        val stompEndpointUrl = "${baseUrl.trimEnd('/')}/in-trip-communication-service/ws/stomp"
        Log.d(TAG, "Connecting to STOMP URL: $stompEndpointUrl")

        stompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            stompEndpointUrl,
            null,
            okHttpClient
        ).withClientHeartbeat(10000).withServerHeartbeat(10000)

        stompClient?.lifecycle()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeBy(
                onNext = { lifecycleEvent ->
                    when (lifecycleEvent.type) {
                        LifecycleEvent.Type.OPENED -> {
                            Log.d(TAG, "STOMP connection opened to $stompEndpointUrl")
                            subscribeToChatTopic(chatId)
                        }
                        LifecycleEvent.Type.ERROR -> {
                            Log.e(TAG, "STOMP connection error to $stompEndpointUrl: ${lifecycleEvent.exception?.message}", lifecycleEvent.exception)
                        }
                        LifecycleEvent.Type.CLOSED -> {
                            Log.d(TAG, "STOMP connection closed for $stompEndpointUrl")
                            compositeDisposable.clear()
                        }
                        else -> {
                            Log.d(TAG, "STOMP lifecycle event: ${lifecycleEvent.type} for $stompEndpointUrl")
                        }
                    }
                },
                onError = { throwable ->
                    Log.e(TAG, "STOMP lifecycle subscription error for $stompEndpointUrl", throwable)
                }
            )?.addTo(compositeDisposable)

        Log.d(TAG, "Attempting to connect STOMP client to $stompEndpointUrl...")
        stompClient?.connect()
    }

    private fun subscribeToChatTopic(chatId: String) {
        if (stompClient?.isConnected != true) {
            Log.w(TAG, "Cannot subscribe to topic, STOMP client not connected.")
            return
        }
        val topicPath = "/in-trip-communication-service/topic/chats/$chatId"
        Log.d(TAG, "Subscribing to topic: $topicPath")
        stompClient?.topic(topicPath)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(Schedulers.computation())
            ?.subscribeBy(
                onNext = { frame ->
                    try {
                        val dto = gson.fromJson(frame.payload, MessageDto::class.java)
                        serviceScope.launch(Dispatchers.Main) {
                            _incoming.emit(dto.toDomain())
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Invalid payload received on $topicPath: ${frame.payload}", e)
                    }
                },
                onError = { err ->
                    Log.e(TAG, "Error on STOMP topic subscription for $topicPath", err)
                }
            )?.addTo(compositeDisposable)
    }

    private fun disconnect() {
        Log.d(TAG, "Disconnecting STOMP client (currentChatId: $currentChatId)...")
        currentChatId = null
        compositeDisposable.clear()
        try {
            if (stompClient?.isConnected == true) {
                stompClient?.disconnect()
                Log.d(TAG, "STOMP disconnect call initiated.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception while trying to disconnect STOMP client", e)
        } finally {
            stompClient = null
        }
    }

    fun cleanup() {
        Log.d(TAG, "Cleaning up InTripSocketService resources.")
        disconnect()
        serviceScope.cancel()
    }
}