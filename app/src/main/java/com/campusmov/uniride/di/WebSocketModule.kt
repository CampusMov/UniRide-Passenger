package com.campusmov.uniride.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {
    @Provides
    @Singleton
    fun provideStompClient(): StompClient {
        return StompClient(OkHttpWebSocketClient())
    }
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}