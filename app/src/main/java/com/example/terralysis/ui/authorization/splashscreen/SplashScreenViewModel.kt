package com.example.terralysis.ui.authorization.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.terralysis.data.local.entity.AuthEntity
import com.example.terralysis.data.repository.AuthRepository

class SplashScreenViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun getAuthData() : LiveData<AuthEntity> {
        return authRepository.checkAuthState().asLiveData()
    }
}