package com.example.terralysis.data.remote.retrofit

import com.example.terralysis.data.remote.response.HistoryResponse
import com.example.terralysis.data.remote.response.ScanRequestResponse
import com.example.terralysis.data.remote.response.ScanResultResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface ScanApiService {

    //Get all scan history
    @GET("analysis/{userId}")
    suspend fun getScanHistory(
        @Path("userId") userId: String
    ) : HistoryResponse

    //Used when sending pic to server to analyze
    @Multipart
    @POST("analysis")
    suspend fun scanRequest(
        @Query ("userId") userId : String,
        @Part file: MultipartBody.Part
    ): ScanRequestResponse

    //Get individual scan result
    @GET("analysis/{userId}/{imageId}")
    suspend fun getScanResult(
        @Path("userId") userId: String,
        @Path("imageId") imageId : String
    ) : ScanResultResponse
}