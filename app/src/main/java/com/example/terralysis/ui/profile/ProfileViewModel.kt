package com.example.terralysis.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.terralysis.data.local.entity.UserEntity
import com.example.terralysis.data.repository.AuthRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun getUserData() : LiveData<UserEntity> {
        return authRepository.getUserData().asLiveData()
    }
    fun getAuthData() = authRepository.checkAuthState()

    fun logout(){
        viewModelScope.launch{
            authRepository.logout()
        }
    }
}