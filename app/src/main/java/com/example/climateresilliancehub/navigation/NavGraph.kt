package com.example.climateresilliancehub

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.climateresiliencehub.screens.RegisterScreen
import com.example.climateresilliancehub.models.Report
import com.example.climateresilliancehub.ui.theme.Screens.Dashboard.DashboardScreen

import com.example.climateresilliancehub.ui.theme.Screens.Reports.AddReportScreen
import com.example.climateresilliancehub.ui.theme.Screens.Reports.UpdateReportScreen
import com.example.climateresilliancehub.ui.theme.Screens.Reports.ViewReportsScreen


import com.example.climateresilliancehub.ui.theme.Screens.login.LoginScreen
import com.google.gson.Gson

sealed class Screen(val route: String) {
    object Register : Screen("register")
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object AddReport : Screen("add_report")
    object ViewReports : Screen("view_reports")
    object UpdateReport : Screen("update_report/{reportJson}") {
        fun createRoute(report: Report): String {
            val reportJson = Gson().toJson(report)
            return "update_report/$reportJson"
        }
    }
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Register.route) {

        // Register Screen
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }

        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        // Dashboard Screen
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }

        // Add Report Screen
        composable(Screen.AddReport.route) {
            AddReportScreen { report ->
                // After adding, navigate to ViewReports
                navController.navigate(Screen.ViewReports.route)
            }
        }

        // View Reports Screen
        composable(Screen.ViewReports.route) {
            val sampleReports = listOf(
                Report("1","user1","Flood in Market","Severe flood affecting main market","Downtown","https://res.cloudinary.com/dzrlsvlai/image/upload/sample.jpg"),
                Report("2","user2","Fallen Tree","Tree fell blocking the road","5th Avenue","")
            )
            ViewReportsScreen(reports = sampleReports)
        }

        // Update Report Screen
        composable(
            route = Screen.UpdateReport.route,
            arguments = listOf(navArgument("reportJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val reportJson = backStackEntry.arguments?.getString("reportJson")
            val report = Gson().fromJson(reportJson, Report::class.java)
            UpdateReportScreen(report) { updatedReport ->
                // Handle updated report
                navController.navigate(Screen.ViewReports.route)
            }
        }
    }
}
