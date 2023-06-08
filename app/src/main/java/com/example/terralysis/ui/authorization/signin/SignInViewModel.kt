package com.example.terralysis.ui.authorization.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.terralysis.data.local.entity.AuthEntity
import com.example.terralysis.data.repository.AuthRepository

class SignInViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun signIn(
        email: String,
        password : String,
    ) = authRepository.signIn(email, password)

    fun getAuthData() : LiveData<AuthEntity>{
        return authRepository.checkAuthState().asLiveData()
    }
}