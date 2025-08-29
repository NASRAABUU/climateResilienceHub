package com.example.climateresiliencehub.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.climateresiliencehub.model.Report
import com.example.climateresiliencehub.viewmodel.ReportViewModel

@Composable
fun ViewReportsScreen(
    viewModel: ReportViewModel = viewModel(),
    onEdit: (Report) -> Unit,
    onView: (Report) -> Unit
) {
    val reports by viewModel.reports.collectAsState(initial = emptyList())
    val loading by viewModel.loading.collectAsState(initial = false)

    var showDeleteDialog by remember { mutableStateOf(false) }
    var reportToDelete by remember { mutableStateOf<Report?>(null) }

    // Load reports only once
    LaunchedEffect(Unit) { viewModel.loadReports() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        } else if (reports.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text("No reports found") }
        } else {
            LazyColumn {
                items(reports) { report ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(report.title, style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(4.dp))
                            Text(report.description, style = MaterialTheme.typography.bodyMedium)

                            if (!report.imageUrl.isNullOrEmpty()) {
                                Spacer(Modifier.height(8.dp))
                                AsyncImageFromUrl(report.imageUrl)
                            }

                            Spacer(Modifier.height(8.dp))
                            Row {
                                Button(onClick = { onView(report) }) { Text("Delete") }
                                Spacer(Modifier.width(8.dp))
                                Button(onClick = { onEdit(report) }) { Text("Update") }
                                Spacer(Modifier.width(8.dp))
                                Button(onClick = {
                                    reportToDelete = report
                                    showDeleteDialog = true
                                }) { Text("Delete") }
                            }
                        }
                    }
                }
            }
        }

        // Delete dialog
        if (showDeleteDialog && reportToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirm Delete") },
                text = { Text("Delete '${reportToDelete?.title}'?") },
                confirmButton = {
                    TextButton(onClick = {
                        reportToDelete?.let { viewModel.deleteReport(it.id) }
                        showDeleteDialog = false
                    }) { Text("Yes") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) { Text("No") }
                }
            )
        }
    }
}
