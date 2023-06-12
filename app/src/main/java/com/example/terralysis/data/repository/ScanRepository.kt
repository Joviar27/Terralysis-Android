package com.example.terralysis.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.terralysis.data.ResultState
import com.example.terralysis.data.local.entity.ScanEntity
import com.example.terralysis.data.local.room.ScanDatabase
import com.example.terralysis.data.remote.response.HistoryResponse
import com.example.terralysis.data.remote.response.ScanRequestResponse
import com.example.terralysis.data.remote.response.ScanResultResponse
import com.example.terralysis.data.remote.retrofit.ScanApiService
import com.example.terralysis.util.dateFormatter
import okhttp3.MultipartBody

class ScanRepository(
    private val apiService: ScanApiService,
    private val scanDatabase: ScanDatabase,
) {

    fun getScanHistory(
        userId : String
    ): LiveData<ResultState<List<ScanEntity>>> = liveData {
        emit(ResultState.Loading)
        try {
            val responseHistory = fetchScanHistory(userId)
            val history = mapResponseToScanEntities(responseHistory)

            insertAllScans(history)

            emitSource(getLocalScanHistory())
        } catch (e: Exception) {
            emitSource(getLocalScanHistory())
            emit(ResultState.Error(e.message.toString()))
        }
    }

    private fun mapResponseToScanEntities(historyResponse: HistoryResponse): List<ScanEntity> {
        return historyResponse.image?.map { response ->
            ScanEntity(
                id = response.imageId,
                timestamp = dateFormatter(response.createdAt),
                uri = response.url,
                name = response.kelas
            )
        } ?: emptyList()
    }

    suspend fun deleteAllScans() {
        scanDatabase.scanDao().deleteAll()
    }

    private suspend fun insertAllScans(scanEntities: List<ScanEntity>) {
        scanDatabase.scanDao().insertScans(scanEntities)
    }

    private suspend fun fetchScanHistory(userId: String): HistoryResponse {
        return apiService.getScanHistory(userId)
    }

    private fun getLocalScanHistory(): LiveData<ResultState<List<ScanEntity>>> {
        return scanDatabase.scanDao().getScanHistory().map {
            ResultState.Success(it)
        }
    }

    fun addScanRequest(
        imageMultipart: MultipartBody.Part,
        userId: String
    ): LiveData<ResultState<ScanEntity>> = liveData {
        emit(ResultState.Loading)
        try {
            val scanRequestResponse = submitScanRequest(imageMultipart, userId)
            val result = convertRequestToScanEntities(scanRequestResponse)

            insertScan(result)

            emitSource(getLocalScan(result.id))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message as String))
            Log.e(TAG, e.message as String)
        }
    }

    private suspend fun submitScanRequest(imageMultipart: MultipartBody.Part, userId: String): ScanRequestResponse {
        return apiService.scanRequest(userId, imageMultipart)
    }

    private suspend fun insertScan(scanEntity: ScanEntity){
        scanDatabase.scanDao().insertScan(scanEntity)
    }

    private fun convertRequestToScanEntities(response: ScanRequestResponse): ScanEntity {
        return ScanEntity(
                id = response.imageId,
                timestamp = "",
                uri = response.url,
                name = response.kelas,
            )
        }

    fun getScanResult(
        userId: String,
        imageId: String
    ): LiveData<ResultState<ScanEntity>> = liveData {
        emit(ResultState.Loading)
        try {
            val scanResultResponse = fetchScanResult(userId, imageId)
            val scan = convertResultToScanEntities(scanResultResponse)

            updateScan(scan)

            emitSource(getLocalScan(imageId))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message as String))
            emitSource(getLocalScan(imageId))
        }
    }

    private suspend fun fetchScanResult(userId: String, imageId : String) : ScanResultResponse {
        return apiService.getScanResult(userId, imageId)
    }

    private suspend fun updateScan(scanEntity: ScanEntity){
        scanDatabase.scanDao().updateScan(scanEntity)
    }

    private fun getLocalScan(imageId: String) : LiveData<ResultState<ScanEntity>>{
        return scanDatabase.scanDao().getSingleResult(imageId).map {
            ResultState.Success(it)
        }
    }

    private fun convertResultToScanEntities(response: ScanResultResponse): ScanEntity {
        return ScanEntity(
            id = response.image.imageId,
            timestamp = dateFormatter(response.image.createdAt),
            uri = response.image.url,
            name = response.image.kelas,
            shortDesc = response.image.shortDesc,
            longDesc = response.image.longDesc,
            physicalCar = response.image.ciriFisik,
            chemicalChar = response.image.ciriKimia,
            morphologyChar = response.image.ciriMorfologi,
            spread = response.image.persebaran,
            property = response.image.kandungan
        )
    }

    companion object{
        const val TAG = "ScanRepository"

        @Volatile
        private var instance : ScanRepository? = null

        fun getInstance(
            apiService:ScanApiService,
            scanDatabase: ScanDatabase,
        ) : ScanRepository =
            instance ?: synchronized(this){
                instance ?: ScanRepository(apiService,scanDatabase)
            }.also { instance=it }
    }
}