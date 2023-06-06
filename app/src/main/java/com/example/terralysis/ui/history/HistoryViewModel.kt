package com.example.terralysis.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.terralysis.data.local.entity.UserEntity
import com.example.terralysis.data.repository.AuthRepository
import com.example.terralysis.data.repository.ScanRepository

class HistoryViewModel(
    private val scanRepository: ScanRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    fun getUserData() : LiveData<UserEntity>{
        return authRepository.getUserData().asLiveData()
    }

    fun getHistory(userId : String) = scanRepository.getScanHistory(userId)
}