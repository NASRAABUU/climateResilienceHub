package com.example.climateresilliancehub.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climateresilliancehub.models.Report

import com.example.climateresilliancehub.network.RetrofitInstance
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ReportViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance().getReference("reports")

    private val _reports = MutableLiveData<List<Report>>(emptyList())
    val reports: LiveData<List<Report>> get() = _reports

    private val _isUploading = MutableLiveData(false)
    val isUploading: LiveData<Boolean> get() = _isUploading

    // Add new report with optional image upload
    fun addReport(report: Report, imagePath: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _isUploading.postValue(true)
            try {
                var finalReport = report

                // Upload image to Cloudinary if imagePath is provided
                if (!imagePath.isNullOrEmpty()) {
                    val file = File(imagePath)
                    val requestFile: RequestBody =
                        file.asRequestBody("image/*".toMediaTypeOrNull())
                    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                    val preset: RequestBody = RequestBody.create(
                        "text/plain".toMediaTypeOrNull(),
                        "resiliance_preset"
                    )

                    val response = RetrofitInstance.cloudinaryApi.uploadImage(body, preset)
                    val imageUrl: String = response.body()?.secureUrl ?: ""

                    finalReport = report.copy(imageUrl = imageUrl)
                }

                // Push to Firebase
                val newRef = database.push()
                finalReport = finalReport.copy(reportId = newRef.key ?: "")
                newRef.setValue(finalReport)

                // Update LiveData
                val updatedList = _reports.value.orEmpty().toMutableList().apply { add(finalReport) }
                _reports.postValue(updatedList)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isUploading.postValue(false)
            }
        }
    }

    // Update an existing report
    fun updateReport(report: Report) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Update Firebase
                database.child(report.reportId).setValue(report)

                // Update LiveData
                val updatedList = _reports.value?.map {
                    if (it.reportId == report.reportId) report else it
                }
                _reports.postValue(updatedList ?: emptyList())

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Delete a report
    fun deleteReport(report: Report) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Delete from Firebase
                database.child(report.reportId).removeValue()

                // Update LiveData
                val updatedList = _reports.value?.filter { it.reportId != report.reportId }
                _reports.postValue(updatedList ?: emptyList())

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Load all reports from Firebase
    fun fetchReports() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.get().addOnSuccessListener { snapshot ->
                    val list = mutableListOf<Report>()
                    for (child in snapshot.children) {
                        val report = child.getValue(Report::class.java)
                        report?.let { list.add(it) }
                    }
                    _reports.postValue(list)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
