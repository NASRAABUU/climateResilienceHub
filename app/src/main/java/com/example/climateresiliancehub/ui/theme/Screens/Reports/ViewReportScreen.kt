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
    val reports by viewModel.reports.collectAsState()
    val loading by viewModel.loading.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var reportToDelete by remember { mutableStateOf<Report?>(null) }
    var updateMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadReports()
    }

    Column(Modifier.fillMaxSize().padding(8.dp)) {

        if (loading) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
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
                            Text(report.description)
                            Spacer(Modifier.height(4.dp))
                            Text(report.imageUrl)

                            Spacer(Modifier.height(8.dp))
                            Row {
                                Button(onClick = { onView(report) }) { Text("View") }
                                Spacer(Modifier.width(8.dp))
                                Button(onClick = {
                                    onEdit(report)
                                    updateMessage = "Updated Successfully!"
                                }) { Text("Update") }
                                Spacer(Modifier.width(8.dp))
                                Button(onClick = {
                                    reportToDelete = report
                                    showDeleteDialog = true
                                }) { Text("Delete") }
                            }

                            if (updateMessage.isNotEmpty()) {
                                Spacer(Modifier.height(4.dp))
                                Text(updateMessage, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog && reportToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirm Delete") },
                text = { Text("Are you sure you want to delete '${reportToDelete?.title}'?") },
                confirmButton = {
                    TextButton(onClick = {
                        reportToDelete?.let { viewModel.deleteReport(it.id) }
                        showDeleteDialog = false
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
    }
}
