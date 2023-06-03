package com.example.terralysis.data.remote.retrofit

import com.example.terralysis.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

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

    //Used when sending pic to server to analyze
    @Multipart
    @POST("Add Route here")
    suspend fun scanRequest(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): ScanRequestResponse

    //Get the individual detail of scan result
    @GET("Add Route Here")
    suspend fun getScanResult(
        //TODO "Add path/query"
    ) : ScanResultResponse

    //Get all scan history
    @GET("Add Route Here")
    suspend fun getScanHistory() : HistoryResponse
}