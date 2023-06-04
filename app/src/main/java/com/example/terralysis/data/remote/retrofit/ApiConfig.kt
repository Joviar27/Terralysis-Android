package com.example.terralysis.data.remote.retrofit

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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

    private var BASE_URL : String = "https://terralysis-api-v2-l22usupt4a-uc.a.run.app"

    fun getApiService(authPreference: AuthPreference) : ApiService {

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
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}