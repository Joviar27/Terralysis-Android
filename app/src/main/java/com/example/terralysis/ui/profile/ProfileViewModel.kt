package com.example.terralysis.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.terralysis.data.repository.AuthRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    fun getUserData() = authRepository.getUserData()

    fun getAuthData() = authRepository.checkAuthState()

    fun logout(){
        viewModelScope.launch{
            authRepository.logout()
        }
    }
}