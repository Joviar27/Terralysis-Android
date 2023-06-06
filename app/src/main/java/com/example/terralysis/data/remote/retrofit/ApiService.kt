package com.example.terralysis.data.remote.retrofit

import com.example.terralysis.data.remote.response.*
import okhttp3.MultipartBody
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

    //Get all scan history
    @GET("images/{userId}")
    suspend fun getScanHistory(
        @Path ("userId") userId: String
    ) : HistoryResponse

    //TODO "MASIH DUMMY"
    //Used when sending pic to server to analyze
    @Multipart
    @POST("images")
    suspend fun scanRequest(
        @Part file: MultipartBody.Part,
    ): ScanRequestResponse


    /*TODO "MASIH DUMMY"
    //Get the individual detail of scan result
    @GET("images/{userId}/{imageId}")
    suspend fun getScanResult(
        @Path ("userId") userId: String,
        @Path ("imageId") imageId: String
    ) : ScanResultResponse
     */
}