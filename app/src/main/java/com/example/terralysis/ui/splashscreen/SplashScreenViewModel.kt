package com.example.terralysis.ui.splashscreen

import androidx.lifecycle.ViewModel
import com.example.terralysis.data.repository.AuthRepository

class SplashScreenViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun checkAuthState() = authRepository.checkAuthState()
}