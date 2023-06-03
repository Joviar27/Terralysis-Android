package com.example.terralysis.ui.detail

import androidx.lifecycle.ViewModel
import com.example.terralysis.data.repository.ScanRepository

class DetailViewModel(
    private val scanRepository: ScanRepository
) : ViewModel() {
    fun getScanResult(
        //TODO "Add Parameter Here"
    ) = scanRepository.getScanResult(
        //TODO "Add parameter"
    )
}