package com.example.terralysis.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.terralysis.data.ResultState
import com.example.terralysis.data.local.entity.ScanEntity
import com.example.terralysis.data.local.room.ScanDatabase
import com.example.terralysis.data.remote.response.HistoryResponse
import com.example.terralysis.data.remote.response.ScanRequestResponse
import com.example.terralysis.data.remote.retrofit.ApiService
import okhttp3.MultipartBody

class ScanRepository(
    private val apiService: ApiService,
    private val scanDatabase: ScanDatabase,
) {
    fun getScanHistory(
        userId : String
    ): LiveData<ResultState<List<ScanEntity>>> = liveData {
        emit(ResultState.Loading)
        try {
            val responseHistory = fetchScanHistory(userId)
            when(responseHistory.error) {
                true -> emit(ResultState.Error(responseHistory.message))
                false -> {
                    val history = mapResponseToScanEntities(responseHistory)

                    deleteAllScans()

                    insertAllScans(history)

                    emitSource(getLocalScanHistory())
                }
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    private fun mapResponseToScanEntities(responseHistory: HistoryResponse): List<ScanEntity> {
        return responseHistory.data.map { response ->
            ScanEntity(
                id = response.imageId,
                timestamp = response.createdAt,
                uri = response.url,
                //Waiting for API to be completed
                name = "Dummy sambil nunggu API",
                shortDesc = "Dummy sambil nunggu API",
                longDesc = "Dummy sambil nunggu API"
            )
        }
    }

    private suspend fun deleteAllScans() {
        scanDatabase.scanDao().deleteAll()
    }

    private suspend fun insertAllScans(scanEntities: List<ScanEntity>) {
        scanDatabase.scanDao().insertScan(scanEntities)
    }

    private suspend fun fetchScanHistory(userId: String): HistoryResponse {
        return apiService.getScanHistory(userId)
    }

    private fun getLocalScanHistory(): LiveData<ResultState<List<ScanEntity>>> {
        return scanDatabase.scanDao().getScanHistory().map {
            ResultState.Success(it)
        }
    }

    //This one still call the dummy API endpoint
    fun addScanRequest(
        imageMultipart: MultipartBody.Part,
    ): LiveData<ResultState<ScanEntity>> = liveData {
        emit(ResultState.Loading)
        try{
            val response = submitScanRequest(imageMultipart)

            when(response.error){
                true -> emit(ResultState.Error(response.message))
                false -> {
                    val scanResult = convertResponseToScanEntities(response)

                    val remoteResponse = MutableLiveData<ResultState<ScanEntity>>()
                    remoteResponse.value = ResultState.Success(scanResult)

                    emitSource(remoteResponse)
                }
            }
        }
        catch (e : Exception){
            emit(ResultState.Error(e.message.toString()))
        }
    }

    private suspend fun submitScanRequest(imageMultipart: MultipartBody.Part): ScanRequestResponse {
        return apiService.scanRequest(imageMultipart)
    }

    private fun convertResponseToScanEntities(scanResult : ScanRequestResponse): ScanEntity {
        return ScanEntity(
                id = scanResult.data.imageId,
                timestamp = scanResult.data.createdAt,
                uri = scanResult.data.url,
                //Waiting for API to be completed
                name = "Dummy sambil nunggu API",
                shortDesc = "Dummy sambil nunggu API",
                longDesc = "Dummy sambil nunggu API"
            )
        }

    /*This one is a dummy to get individual scan result
    Not sure we'll ever need this one
    fun getScanResult(
        userId: String,
        imageId: String
    ): LiveData<ResultState<ScanResultResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = fetchScanResult(userId, imageId)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    private suspend fun fetchScanResult(userId: String, imageId: String): ScanResultResponse {
        return apiService.getScanResult(userId, imageId)
    }
     */

    companion object{
        @Volatile
        private var instance : ScanRepository? = null

        fun getInstance(
            apiService:ApiService,
            scanDatabase: ScanDatabase,
        ) : ScanRepository =
            instance ?: synchronized(this){
                instance ?: ScanRepository(apiService,scanDatabase)
            }.also { instance=it }
    }
}