package com.example.terralysis.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.terralysis.data.ResultState
import com.example.terralysis.data.local.datastore.AuthPreference
import com.example.terralysis.data.local.datastore.UserPreference
import com.example.terralysis.data.local.entity.AuthEntity
import com.example.terralysis.data.local.entity.UserEntity
import com.example.terralysis.data.remote.response.SignInResponse
import com.example.terralysis.data.remote.response.SignUpResponse
import com.example.terralysis.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

class AuthRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val authPreference: AuthPreference
    ){
    fun signUp(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultState<SignUpResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val responseRegister = apiService.signUp(name, email, password)
            when(responseRegister.error){
                true -> emit(ResultState.Error(responseRegister.message?:"Registrasi Gagal"))
                else ->{
                    val remoteResponse = MutableLiveData<ResultState<SignUpResponse>>()
                    remoteResponse.value = ResultState.Success(responseRegister)
                    emitSource(remoteResponse)
                }
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun signIn(
        email: String,
        password: String
    ) : LiveData<ResultState<SignInResponse>> = liveData {
        emit(ResultState.Loading)
        try{
            val responseLogin = apiService.signIn(email,password)

            /* TODO"Handle the response similar way to the code below"
            when(responseLogin.error){
                true -> {
                    emit(ResultState.Error(responseLogin.message))
                }
                false -> {
                    val remoteResponse = MutableLiveData<ResultState<LoginResponse>>()
                    remoteResponse.value = ResultState.Success(responseLogin)

                    val auth = AuthEntity(
                        token = responseLogin.loginResult.token,
                        state = true
                    )

                    val user = UserEntity(
                        name = responseLogin.name,
                        email = responseLogin.email
                    )

                    userPreference.setAuth(auth)
                    authPreference.setUser(user)

                    emitSource(remoteResponse)
                }
            }
            */
        }
        catch (e : Exception){
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun getUserData(): LiveData<ResultState<UserEntity?>> = liveData {
        emit(ResultState.Loading)
        try {
            val user = userPreference.userFlow.firstOrNull()
            if (user == null) {
                emit(ResultState.Error("User is null"))
            } else {
                val userData = MutableLiveData<ResultState<UserEntity?>>()
                userData.value = ResultState.Success(user)
                emitSource(userData)
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun checkAuthState(): LiveData<ResultState<AuthEntity?>> = liveData {
        emit(ResultState.Loading)
        try {
            val auth = authPreference.authFlow.firstOrNull()
            if (auth == null) {
                emit(ResultState.Error("User is null"))
            } else {
                val authState = MutableLiveData<ResultState<AuthEntity?>>()
                authState.value = ResultState.Success(auth)
                emitSource(authState)
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    companion object{
        @Volatile
        private var instance : AuthRepository? = null

        fun getInstance(
            apiService:ApiService,
            authPreference: AuthPreference,
            userPreference: UserPreference
        ) : AuthRepository =
            instance ?: synchronized(this){
                instance ?: AuthRepository(apiService,userPreference,authPreference)
            }.also { instance=it }
    }
}