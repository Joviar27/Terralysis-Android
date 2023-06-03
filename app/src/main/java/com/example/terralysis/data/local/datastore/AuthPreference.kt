package com.example.terralysis.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.terralysis.data.local.entity.AuthEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPreference(private val dataStore: DataStore<Preferences>){

    private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
    private val AUTH_STATE_KEY = booleanPreferencesKey("auth_state")

    val authFlow: Flow<AuthEntity> = dataStore.data
        .map { preference ->
            AuthEntity(
                preference[AUTH_STATE_KEY] ?: false,
                preference[AUTH_TOKEN_KEY] ?: "",
            )
        }

    suspend fun setAuth(authEntity: AuthEntity) {
        dataStore.edit {
            it[AUTH_STATE_KEY] = authEntity.state
            it[AUTH_TOKEN_KEY] = authEntity.token
        }
    }

    suspend fun clearAuth() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object{
        @Volatile
        private var INSTANCE : AuthPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): AuthPreference{
            return INSTANCE ?: synchronized(this){
                val instance = AuthPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}