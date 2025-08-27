package com.example.climateresilliancehub.ui.theme.Screens.Reports

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.climateresilliancehub.R
import com.example.climateresilliancehub.models.Report

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewReportsScreen(
    reports: List<Report>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("View Reports") }) }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reports) { report ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "Title: ${report.title}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Description: ${report.description}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Location: ${report.location}", style = MaterialTheme.typography.bodyMedium)

                        // For Preview use placeholder image
                        val imagePainter = if (report.imageUrl.isNotEmpty()) {
                            painterResource(id = R.drawable.back) // Replace with a local drawable
                        } else null

                        imagePainter?.let {
                            Image(
                                painter = it,
                                contentDescription = "Report Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewReportsScreenPreview() {
    val sampleReports = listOf(
        Report(
            title = "Flood in Market",
            description = "Severe flood affecting main market",
            location = "Downtown",
            imageUrl = "sample" // Preview uses placeholder drawable
        ),
        Report(
            title = "Fallen Tree",
            description = "Tree fell blocking the road",
            location = "5th Avenue",
            imageUrl = ""
        )
    )
    ViewReportsScreen(reports = sampleReports)
}
