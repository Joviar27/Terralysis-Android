package com.example.terralysis.ui.profile

import com.example.terralysis.data.repository.AuthRepository
import com.example.terralysis.data.repository.ScanRepository

class ProfileViewModel(
    private val authRepository: AuthRepository
) {
    fun getUserData() = authRepository.getUserData()
}