package com.example.climateresilliancehub.ui.theme.Screens.Reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.climateresilliancehub.models.Report

@OptIn(ExperimentalMaterial3Api::class) // Add this line to remove TopAppBar error
@Composable
fun UpdateReportScreen(
    report: Report,                        // Report to update
    onUpdateReport: (Report) -> Unit = {}  // Callback after update
) {
    var title by remember { mutableStateOf(report.title) }
    var description by remember { mutableStateOf(report.description) }
    var location by remember { mutableStateOf(report.location) }
    var imageUrl by remember { mutableStateOf(report.imageUrl) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Update Report") })
        }
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
                    modifier = Modifier.padding(16.dp),
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
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("Image URL (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                    )

                    Button(
                        onClick = {
                            val updatedReport = report.copy(
                                title = title,
                                description = description,
                                location = location,
                                imageUrl = imageUrl
                            )
                            onUpdateReport(updatedReport)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Update Report")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateReportScreenPreview() {
    val dummyReport = Report(
        reportId = "1",
        title = "Sample Title",
        description = "Sample Description",
        location = "Nairobi",
        imageUrl = "https://example.com/image.jpg"
    )
    UpdateReportScreen(report = dummyReport) { updatedReport ->
        println("Updated Report: $updatedReport")
    }
}
