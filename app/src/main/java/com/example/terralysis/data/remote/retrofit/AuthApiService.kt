package com.example.terralysis.data.remote.retrofit

import com.example.terralysis.data.remote.response.*
import retrofit2.http.*

interface AuthApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun signUp(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password: String
    ) : SignUpResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun signIn(
        @Field("email") email : String,
        @Field("password") password: String
    ) : SignInResponse
}