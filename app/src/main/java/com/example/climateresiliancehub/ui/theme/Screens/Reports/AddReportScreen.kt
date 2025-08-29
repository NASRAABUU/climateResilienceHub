package com.example.climateresiliencehub.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.climateresiliencehub.model.Report
import com.example.climateresiliencehub.viewmodel.ReportViewModel
import java.io.File

@Composable
fun AddReportScreen(
    viewModel: ReportViewModel = viewModel(),
    onReportAdded: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Add Report", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

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
            Spacer(Modifier.height(8.dp))

            Button(onClick = { imagePicker.launch("image/*") }) { Text("Pick Image") }
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val report = Report(title = title, description = description)
                    viewModel.addReport(report, null) // Update if you handle image file
                    onReportAdded()
                },
                enabled = title.isNotBlank() && description.isNotBlank()
            ) { Text("Submit Report") }
        }
    }
}
