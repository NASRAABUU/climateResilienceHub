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

    // List of reports
    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports

    // Loading state
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // Optional error message
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Load all reports
    fun loadReports() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _reports.value = repository.getReports()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    // Add a new report
    fun addReport(report: Report, imageFile: File?) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.addReport(report, imageFile)
                loadReports()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    // Update an existing report
    fun updateReport(report: Report) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.updateReport(report)
                loadReports()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    // Delete a report by ID
    fun deleteReport(id: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.deleteReport(id)
                loadReports()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
