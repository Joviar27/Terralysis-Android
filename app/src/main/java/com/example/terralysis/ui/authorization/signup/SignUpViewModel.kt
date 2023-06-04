package com.example.terralysis.ui.authorization.signup

import androidx.lifecycle.ViewModel
import com.example.terralysis.data.repository.AuthRepository

class SignUpViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun signUp(
        name : String,
        email : String,
        pass : String
    ) = authRepository.signUp(name, email, pass)
}