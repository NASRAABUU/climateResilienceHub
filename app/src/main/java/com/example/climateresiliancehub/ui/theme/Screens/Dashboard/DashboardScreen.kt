package com.example.climateresiliencehub.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.climateresiliencehub.model.Report
import com.example.climateresiliencehub.viewmodel.AuthViewModel
import com.example.climateresiliencehub.viewmodel.ReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    authViewModel: AuthViewModel = viewModel(),
    reportViewModel: ReportViewModel = viewModel(),
    onAddReport: () -> Unit,
    onEditReport: (Report) -> Unit,
    onViewReport: (Report) -> Unit,
    onLogout: () -> Unit
) {
    val reports by reportViewModel.reports.collectAsState()
    val loading by reportViewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        reportViewModel.loadReports()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🌍 Climate Resilience Hub Dashboard") },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddReport) {
                Icon(Icons.Default.Add, contentDescription = "Add Report")
            }
        }
    ) { padding ->

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (reports.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No reports yet. Tap + to add one!", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(reports) { report ->
                    ReportCard(
                        report = report,
                        onEditReport = onEditReport,
                        onViewReport = onViewReport
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportCard(
    report: Report,
    onEditReport: (Report) -> Unit,
    onViewReport: (Report) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(report.title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))
            Text(report.description, style = MaterialTheme.typography.bodyMedium)

            if (report.imageUrl.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                AsyncImageFromUrl(report.imageUrl)
            }

            Spacer(Modifier.height(8.dp))
            Row {
                Button(onClick = { onViewReport(report) }) { Text("View") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { onEditReport(report) }) { Text("Update") }
            }
        }
    }
}

@Composable
fun AsyncImageFromUrl(url: String) {
    Image(
        painter = rememberAsyncImagePainter(url),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    )
}
