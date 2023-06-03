package com.example.terralysis.ui.signin

import androidx.lifecycle.ViewModel
import com.example.terralysis.data.repository.AuthRepository

class SignInViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun signIn(
        name : String,
        email : String,
    ) = authRepository.signIn(name, email)
}