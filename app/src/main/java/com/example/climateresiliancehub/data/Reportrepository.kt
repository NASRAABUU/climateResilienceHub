package com.example.climateresiliencehub.repository

import com.example.climateresiliencehub.cloudinary.CloudinaryApi
import com.example.climateresiliencehub.model.Report
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.File
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class ReportRepository {
    private val db = FirebaseFirestore.getInstance()
    private val reportsRef = db.collection("reports")

    suspend fun addReport(report: Report, imageFile: File?): Boolean {
        return try {
            var imageUrl = ""
            if (imageFile != null) {
                imageUrl = uploadToCloudinary(imageFile) ?: ""
            }
            val docRef = reportsRef.document()
            val newReport = report.copy(id = docRef.id, imageUrl = imageUrl)
            docRef.set(newReport).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getReports(): List<Report> {
        return reportsRef.get().await().toObjects(Report::class.java)
    }

    suspend fun updateReport(report: Report): Boolean {
        return try {
            reportsRef.document(report.id).set(report).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteReport(id: String): Boolean {
        return try {
            reportsRef.document(id).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun uploadToCloudinary(file: File): String? =
        suspendCancellableCoroutine { cont ->
            CloudinaryApi.uploadImage(file) { response ->
                cont.resume(response?.url)
            }
        }
}
