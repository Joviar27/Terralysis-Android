package com.example.terralysis.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.terralysis.data.ResultState
import com.example.terralysis.data.local.entity.ScanEntity
import com.example.terralysis.data.repository.ScanRepository
import okhttp3.MultipartBody

class CameraViewModel(
    private val scanRepository: ScanRepository
) : ViewModel() {
    fun requestScan(
        imageMultipart : MultipartBody.Part,
    ): LiveData<ResultState<ScanEntity>>  {
        return scanRepository.addScanRequest(imageMultipart)
    }
}