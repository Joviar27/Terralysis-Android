package com.example.terralysis.ui.home

import androidx.lifecycle.ViewModel
import com.example.terralysis.data.repository.AuthRepository

class HomeViewModel (
    private val authRepository: AuthRepository
    ) : ViewModel() {
        fun getUserData() = authRepository.getUserData()
}