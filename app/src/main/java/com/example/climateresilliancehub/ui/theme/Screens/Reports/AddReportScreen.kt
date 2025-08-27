package com.example.climateresilliancehub.ui.theme.Screens.Reports

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.climateresilliancehub.network.RetrofitInstance
import com.example.climateresilliancehub.models.Report
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReportScreen(
    onAddReport: (Report) -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var imagePath by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Report") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = imagePath,
                        onValueChange = { imagePath = it },
                        label = { Text("Image File Path") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                    )

                    Button(
                        onClick = {
                            if (imagePath.isBlank()) {
                                Toast.makeText(context, "Enter image path", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            isUploading = true

                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val file = File(imagePath)
                                    val requestFile: RequestBody =
                                        file.asRequestBody("image/*".toMediaTypeOrNull())
                                    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                                    val preset: RequestBody = RequestBody.create(
                                        "text/plain".toMediaTypeOrNull(),
                                        "resiliance_preset"
                                    )

                                    val response = RetrofitInstance.cloudinaryApi.uploadImage(body, preset)
                                    val imageUrl = response.body()?.secureUrl ?: ""

                                    val newReport = Report(
                                        title = title,
                                        description = description,
                                        location = location,
                                        imageUrl = imageUrl
                                    )

                                    // Switch to Main thread to update UI
                                    CoroutineScope(Dispatchers.Main).launch {
                                        onAddReport(newReport)
                                        title = ""
                                        description = ""
                                        location = ""
                                        imagePath = ""
                                        isUploading = false
                                        Toast.makeText(context, "Report added!", Toast.LENGTH_SHORT).show()
                                    }

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    CoroutineScope(Dispatchers.Main).launch {
                                        isUploading = false
                                        Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isUploading
                    ) {
                        Text(if (isUploading) "Uploading..." else "Add Report")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddReportScreenPreview() {
    AddReportScreen { report ->
        println("Preview Report Added: $report")
    }
}
