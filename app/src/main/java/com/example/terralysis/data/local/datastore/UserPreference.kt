package com.example.terralysis.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.terralysis.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference(private val dataStore: DataStore<Preferences>){

    private val ID_KEY = stringPreferencesKey("id")
    private val EMAIL_KEY = stringPreferencesKey("email")
    private val NAME_KEY = stringPreferencesKey("name")

    val userFlow: Flow<UserEntity> = dataStore.data
        .map { preference ->
            UserEntity(
                name = preference[NAME_KEY] ?: "",
                email = preference[EMAIL_KEY] ?: "",
                userId = preference[ID_KEY] ?: ""
            )
        }

    suspend fun setUser(userEntity: UserEntity) {
        dataStore.edit {
            it[NAME_KEY] = userEntity.name
            it[EMAIL_KEY] = userEntity.email
            it[ID_KEY] = userEntity.userId
        }
    }

    suspend fun clearUser() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object{
        @Volatile
        private var INSTANCE : UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference{
            return INSTANCE ?: synchronized(this){
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}