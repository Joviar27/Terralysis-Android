package com.example.terralysis.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.terralysis.data.local.entity.AuthEntity
import com.example.terralysis.data.local.entity.UserEntity
import com.example.terralysis.data.repository.AuthRepository
import com.example.terralysis.data.repository.ScanRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val scanRepository: ScanRepository
) : ViewModel() {
    fun getUserData() : LiveData<UserEntity> {
        return authRepository.getUserData().asLiveData()
    }
    fun getAuthData() : LiveData<AuthEntity> {
        return authRepository.checkAuthState().asLiveData()
    }

    fun deleteLocalData(){
        viewModelScope.launch {
            scanRepository.deleteAllScans()
        }
    }

    fun logout(){
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}