package com.example.climateresiliencehub.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.climateresiliencehub.model.Report
import com.example.climateresiliencehub.viewmodel.ReportViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReportScreen(
    viewModel: ReportViewModel = viewModel(),
    onReportAdded: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageFile by remember { mutableStateOf<File?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Image picker launcher
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        // Convert URI to File if needed for Cloudinary upload
        // imageFile = uri?.let { FileUtils.getFileFromUri(context, it) }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Add New Report", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))

            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            if (imageUri != null) {
                Spacer(Modifier.height(8.dp))
                androidx.compose.foundation.Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
                Spacer(Modifier.height(8.dp))
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = { imagePicker.launch("image/*") }) {
                    Text("Pick Image")
                }

                Button(
                    onClick = {
                        val report = Report(title = title, description = description)
                        viewModel.addReport(report, imageFile)
                        onReportAdded()
                        title = ""
                        description = ""
                        imageFile = null
                        imageUri = null
                    },
                    enabled = title.isNotBlank() && description.isNotBlank()
                ) {
                    Text("Submit")
                }
            }
        }
    }
}
