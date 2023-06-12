package com.example.terralysis.data.remote.retrofit

import com.example.terralysis.BuildConfig
import com.example.terralysis.data.local.datastore.AuthPreference
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    private var AUTH_URL : String = "https://terralysis-final-user-vgz4nva52a-et.a.run.app/"
    private var SCAN_URL : String = "https://terralysis-final-analysis-vgz4nva52a-et.a.run.app/"

    fun getScanApiService(authPreference: AuthPreference) : ScanApiService {

        val pref = authPreference

        val interceptor = if (BuildConfig.DEBUG){
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val authInterceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            val authToken = runBlocking {
                pref.authFlow.firstOrNull()?.token
            }
            if (!authToken.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $authToken")
            }
            chain.proceed(requestBuilder.build())
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(SCAN_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ScanApiService::class.java)
    }

    fun getAuthApiService() : AuthApiService{
        val interceptor = if (BuildConfig.DEBUG){
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(AUTH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(AuthApiService::class.java)
    }
}