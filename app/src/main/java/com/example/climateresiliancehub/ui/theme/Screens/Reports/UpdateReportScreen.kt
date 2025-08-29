package com.example.climateresiliencehub.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.climateresiliencehub.model.Report
import com.example.climateresiliencehub.viewmodel.ReportViewModel

@Composable
fun UpdateReportScreen(
    report: Report,
    viewModel: ReportViewModel = viewModel(),
    onUpdated: () -> Unit
) {
    var title by remember { mutableStateOf(report.title) }
    var description by remember { mutableStateOf(report.description) }

    Column(Modifier.padding(16.dp)) {
        TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
        Spacer(Modifier.height(8.dp))
        TextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            val updated = report.copy(title = title, description = description)
            viewModel.updateReport(updated)
            onUpdated()
        }) {
            Text("Save Changes")
        }
    }
}
