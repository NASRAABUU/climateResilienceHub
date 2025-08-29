package com.example.climateresiliencehub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.climateresiliencehub.model.Report
import com.example.climateresiliencehub.screens.*
import com.example.climateresiliencehub.viewmodel.AuthViewModel
import com.example.climateresiliencehub.viewmodel.ReportViewModel

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val reportViewModel: ReportViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.SPLASH, modifier = modifier) {

        // Splash
        composable(Routes.SPLASH) {
            SplashScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) },
                onNavigateToDashboard = { navController.navigate(Routes.DASHBOARD) }
            )
        }

        // Register
        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { navController.navigate(Routes.DASHBOARD) { popUpTo(Routes.REGISTER) { inclusive = true } } },
                onLoginClick = { navController.navigate(Routes.LOGIN) }
            )
        }

        // Login
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { navController.navigate(Routes.DASHBOARD) { popUpTo(Routes.LOGIN) { inclusive = true } } },
                onRegisterClick = { navController.navigate(Routes.REGISTER) }
            )
        }

        // Dashboard
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                authViewModel = authViewModel,
                reportViewModel = reportViewModel,
                onAddReport = { navController.navigate(Routes.ADD_REPORT) },
                onEditReport = { report ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("report", report)
                    navController.navigate(Routes.UPDATE_REPORT)
                },
                onViewReport = { report ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("report", report)
                    navController.navigate(Routes.VIEW_REPORTS)
                },
                onLogout = { navController.navigate(Routes.LOGIN) { popUpTo(Routes.DASHBOARD) { inclusive = true } } }
            )
        }

        // Add Report
        composable(Routes.ADD_REPORT) {
            AddReportScreen(
                viewModel = reportViewModel,
                onReportAdded = { navController.popBackStack() }
            )
        }

        // Update Report
        composable(Routes.UPDATE_REPORT) {
            val report = navController.previousBackStackEntry?.savedStateHandle?.get<Report>("report")
            report?.let {
                UpdateReportScreen(
                    report = it,
                    viewModel = reportViewModel,
                    onUpdated = { navController.popBackStack() }
                )
            }
        }

        // View Reports
        composable(Routes.VIEW_REPORTS) {
            val report = navController.previousBackStackEntry?.savedStateHandle?.get<Report>("report")
            report?.let {
                ViewReportsScreen(
                    viewModel = reportViewModel,
                    onEdit = { reportToEdit ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("report", reportToEdit)
                        navController.navigate(Routes.UPDATE_REPORT)
                    },
                    onView = { reportToView ->
                        // Optional: you can navigate to a detail screen
                    }
                )
            }
        }
    }
}
