package com.example.terralysis.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.terralysis.data.local.entity.AuthEntity
import com.example.terralysis.data.local.entity.UserEntity
import com.example.terralysis.data.repository.AuthRepository

class HomeViewModel (
    private val authRepository: AuthRepository
    ) : ViewModel() {
    fun getUserData() : LiveData<UserEntity> {
        return authRepository.getUserData().asLiveData()
    }

    fun getAuthData() : LiveData<AuthEntity> {
        return authRepository.checkAuthState().asLiveData()
    }

}