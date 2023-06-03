package com.example.terralysis.ui.history

import androidx.lifecycle.ViewModel
import com.example.terralysis.data.repository.ScanRepository

class HistoryViewModel(
    private val scanRepository: ScanRepository
) : ViewModel() {
    fun getScanHistory(
        //TODO "Add parameter here"
    ) = scanRepository.getScanHistory()
}