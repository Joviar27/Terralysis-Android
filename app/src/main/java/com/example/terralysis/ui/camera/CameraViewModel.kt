package com.example.terralysis.ui.camera

import androidx.lifecycle.ViewModel
import com.example.terralysis.data.repository.ScanRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CameraViewModel(
    private val scanRepository: ScanRepository
) : ViewModel() {
    fun requestScan(
        imageMultipart : MultipartBody.Part,
        description : RequestBody,
    ) = scanRepository.addScanRequest(imageMultipart, description)
}