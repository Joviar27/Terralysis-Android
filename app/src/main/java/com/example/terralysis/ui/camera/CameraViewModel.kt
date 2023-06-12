package com.example.terralysis.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.terralysis.data.ResultState
import com.example.terralysis.data.local.entity.ScanEntity
import com.example.terralysis.data.local.entity.UserEntity
import com.example.terralysis.data.repository.AuthRepository
import com.example.terralysis.data.repository.ScanRepository
import okhttp3.MultipartBody

class CameraViewModel(
    private val scanRepository: ScanRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    fun requestScan(
        imageMultipart : MultipartBody.Part,
        userId : String
    ): LiveData<ResultState<ScanEntity>>  {
        return scanRepository.addScanRequest(imageMultipart, userId)
    }

    fun getUserData() : LiveData<UserEntity>{
        return authRepository.getUserData().asLiveData()
    }
}