package com.example.climateresilliancehub.ui.theme.Screens.Dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import com.example.climateresilliancehub.Screen

@Composable
fun DashboardScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Welcome to Climate Resilience Hub",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate(Screen.ViewReports.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Reports")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate(Screen.AddReport.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Report")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    // For preview only, we pass a dummy NavController
    DashboardScreen(navController = androidx.navigation.compose.rememberNavController())
}
