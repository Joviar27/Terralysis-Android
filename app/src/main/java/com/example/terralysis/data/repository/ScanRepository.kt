package com.example.terralysis.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.terralysis.data.ResultState
import com.example.terralysis.data.local.datastore.UserPreference
import com.example.terralysis.data.local.entity.ScanEntity
import com.example.terralysis.data.local.entity.UserEntity
import com.example.terralysis.data.local.room.ScanDao
import com.example.terralysis.data.remote.response.ScanRequestResponse
import com.example.terralysis.data.remote.response.ScanResultResponse
import com.example.terralysis.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ScanRepository(
    private val apiService: ApiService,
    private val scanDao: ScanDao,
    private val userPreference: UserPreference,
) {
    fun getScanHistory(
        //TODO "Add parameter here"
    ): LiveData<ResultState<List<ScanEntity>>> = liveData{
        emit(ResultState.Loading)
        try{
            val responseHistory = apiService.getScanHistory()

            /* TODO"Handle the response similar way to the code below"
            val history = responseHistory.listScan.map {
                ScanEntity(
                    id = it.id,
                    name = it.name,
                    timestamp = it.timeStamp,
                    uri = it.uri,
                    shortDesc = it.shortDesc,
                    longDesc = it.longDesc
                )
            }

            scanDao.deleteAll()
            scanDao.insertScan(history)

            val localData : LiveData<ResultState<List<ScanEntity>>> =
                scanDao.getScanHistory().map {
                    ResultState.Success(it)
                }
            emitSource(localData)
            */
        }
        catch (e : Exception){
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun addScanRequest(
        imageMultipart: MultipartBody.Part,
        description: RequestBody
    ): LiveData<ResultState<ScanRequestResponse>> = liveData {
        emit(ResultState.Loading)
        try{
            val response = apiService.scanRequest(imageMultipart, description)

            //We can also map the response into a new Entity, for best practice
            //We'll look into it after the API are done

            val remoteResponse = MutableLiveData<ResultState<ScanRequestResponse>>()
            remoteResponse.value = ResultState.Success(response)
            emitSource(remoteResponse)
        }
        catch (e : Exception){
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun getScanResult(
        //TODO "Add parameter here"
    ) : LiveData<ResultState<ScanResultResponse>> = liveData {
        emit(ResultState.Loading)
        try{
            val response = apiService.getScanResult()

            //We can also map the response into a new Entity, for best practice
            //We'll look into it after the API are done

            val remoteResponse = MutableLiveData<ResultState<ScanResultResponse>>()
            remoteResponse.value = ResultState.Success(response)
            emitSource(remoteResponse)
        }
        catch (e : Exception){
            emit(ResultState.Error(e.message.toString()))
        }
    }

    companion object{
        @Volatile
        private var instance : ScanRepository? = null

        fun getInstance(
            apiService:ApiService,
            scanDao: ScanDao,
            userPreference: UserPreference
        ) : ScanRepository =
            instance ?: synchronized(this){
                instance ?: ScanRepository(apiService,scanDao, userPreference)
            }.also { instance=it }
    }
}