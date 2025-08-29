package com.example.climateresiliencehub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climateresiliencehub.model.Report
import com.example.climateresiliencehub.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class ReportViewModel : ViewModel() {
    private val repository = ReportRepository()

    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadReports() {
        viewModelScope.launch {
            _loading.value = true
            _reports.value = repository.getReports()
            _loading.value = false
        }
    }

    fun addReport(report: Report, imageFile: File?) {
        viewModelScope.launch {
            repository.addReport(report, imageFile)
            loadReports()
        }
    }

    fun updateReport(report: Report) {
        viewModelScope.launch {
            repository.updateReport(report)
            loadReports()
        }
    }

    fun deleteReport(id: String) {
        viewModelScope.launch {
            repository.deleteReport(id)
            loadReports()
        }
    }
}
