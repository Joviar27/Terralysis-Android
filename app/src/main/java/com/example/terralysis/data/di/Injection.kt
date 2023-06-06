package com.example.terralysis.data.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.terralysis.data.local.datastore.AuthPreference
import com.example.terralysis.data.local.datastore.UserPreference
import com.example.terralysis.data.local.room.ScanDatabase
import com.example.terralysis.data.remote.retrofit.ApiConfig
import com.example.terralysis.data.repository.AuthRepository
import com.example.terralysis.data.repository.ScanRepository

object Injection {

    private val Context.dataStore by preferencesDataStore(name = "user_preferences")

    fun provideAuthRepository(context: Context): AuthRepository {
        val userPreference = provideUserPreference(context)
        val authPreference = provideAuthPreference(context)

        val apiService = ApiConfig.getApiService(authPreference)
        return AuthRepository.getInstance(apiService, userPreference, authPreference)
    }

    fun provideScanRepository(context: Context) : ScanRepository{
        val authPreference = provideAuthPreference(context)
        val scanDatabase= ScanDatabase.getDatabase(context)

        val apiService = ApiConfig.getApiService(authPreference)
        return ScanRepository.getInstance(apiService, scanDatabase)
    }

    fun provideAuthPreference(context: Context) : AuthPreference{
        val dataStore = context.dataStore
        return AuthPreference.getInstance(dataStore)
    }

    fun provideUserPreference(context: Context) : UserPreference {
        val dataStore = context.dataStore
        return UserPreference.getInstance(dataStore)
    }
}