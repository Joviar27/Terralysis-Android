package com.example.terralysis.ui.authorization.signin

import androidx.lifecycle.ViewModel
import com.example.terralysis.data.repository.AuthRepository

class SignInViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun signIn(
        email: String,
        password : String,
    ) = authRepository.signIn(email, password)

    fun getAuthData() = authRepository.checkAuthState()
}