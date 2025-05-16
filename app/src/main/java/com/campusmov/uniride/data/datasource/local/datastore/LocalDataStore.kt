package com.campusmov.uniride.data.datasource.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.campusmov.uniride.domain.auth.model.AuthVerificationCodeResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataStore(private val dataStore: DataStore<Preferences>) {

    suspend fun saveEmail(email: String) {
        val dataStoreKey = stringPreferencesKey("USER_EMAIL")
        dataStore.edit { pref ->
            pref[dataStoreKey] = email
        }
    }

    fun getEmail(): Flow<String?> {
        val dataStoreKey = stringPreferencesKey("USER_EMAIL")
        return dataStore.data.map { pref ->
            pref[dataStoreKey]
        }
    }

    suspend fun save(authResponse: AuthVerificationCodeResponse){
        val dataStoreKey = stringPreferencesKey("AUTH_KEY")
        dataStore.edit { pref ->
            pref[dataStoreKey] = authResponse.toJson()
        }
    }

    fun getData(): Flow<AuthVerificationCodeResponse>{
        val dataStoreKey = stringPreferencesKey("AUTH_KEY")
        return dataStore.data.map { pref ->
            if ( pref[dataStoreKey] != null) {
                AuthVerificationCodeResponse.fromJson(pref[dataStoreKey]!!)
            } else {
                AuthVerificationCodeResponse()
            }
        }
    }

}