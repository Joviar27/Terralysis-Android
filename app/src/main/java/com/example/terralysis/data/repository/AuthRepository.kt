package com.example.terralysis.data.repository

import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AuthRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val authPreference: AuthPreference
    ){
    fun signUp(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultState<String>> = liveData {
        emit(ResultState.Loading)
        try {
            val responseSignUp = submitSignUpRequest(name, email, password)
            when(responseSignUp.error){
                true -> emit(ResultState.Error(responseSignUp.message))
                false ->{
                    val response = MutableLiveData<ResultState<String>>()
                    response.value = ResultState.Success(responseSignUp.message)
                    emitSource(response)
                }
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message as String))
            Log.e(TAG, e.message as String)
        }
    }

    private suspend fun submitSignUpRequest(name : String, email : String, password: String): SignUpResponse {
        return apiService.signUp(name, email, password)
    }

    fun signIn(
        email: String,
        password: String
    ) : LiveData<ResultState<String>> = liveData {
        emit(ResultState.Loading)
        try{
            val responseSignIn = submitSignInRequest(email, password)
            when(val result = handleSignInResponse(responseSignIn)){
                is ResultState.Error -> emit(result)
                is ResultState.Success -> {
                    val response = MutableLiveData<ResultState<String>>()
                    response.value = result
                    emitSource(response)
                }
                else -> {
                    emit(result)
                }
            }
        }
        catch (e : Exception){
            emit(ResultState.Error(e.message as String))
            Log.e(TAG, e.message as String)
        }
    }

    private suspend fun submitSignInRequest(email : String, password: String): SignInResponse {
        return apiService.signIn(email, password)
    }

    private suspend fun handleSignInResponse(responseSignIn: SignInResponse) : ResultState<String>{
        return when (responseSignIn.error) {
            true -> ResultState.Error(responseSignIn.message)
            false -> {
                setUserData(
                    responseSignIn.loginResult.userId,
                    responseSignIn.loginResult.name,
                    responseSignIn.loginResult.email,
                    responseSignIn.loginResult.token
                )
                ResultState.Success(responseSignIn.message)
            }
        }
    }

    //All the function below this can be separated into a new repository, but since the app scale still small, i think it's still unnecessary

    private suspend fun setUserData(
        userId : String,
        name : String,
        email : String,
        token : String
    ) {
        withContext(Dispatchers.IO){
            try{
                val user = UserEntity(
                    userId = userId,
                    name = name,
                    email = email
                )
                val auth = AuthEntity(
                    token = token,
                    state = true
                )
                userPreference.setUser(user)
                authPreference.setAuth(auth)
            }
            catch (e : Exception){
                Log.e(TAG,e.message.toString())
            }
        }
    }

    fun getUserData() : Flow<UserEntity> {
        return userPreference.userFlow
    }

    /* I think the function below is overly complex when it's just reading simple data from datastore
    fun getUserData() : LiveData<ResultState<UserEntity>> = liveData{
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
     */

    fun checkAuthState(): Flow<AuthEntity> {
        return authPreference.authFlow
    }

    suspend fun logout() {
        authPreference.clearAuth()
        userPreference.clearUser()
    }

    companion object{
        private const val TAG = "AuthRepository"

        @Volatile
        private var instance : AuthRepository? = null

        fun getInstance(
            apiService:ApiService,
            userPreference: UserPreference,
            authPreference: AuthPreference
        ) : AuthRepository =
            instance ?: synchronized(this){
                instance ?: AuthRepository(apiService, userPreference, authPreference)
            }.also { instance=it }
    }
}