package com.example.climateresilliancehub.navigation

sealed class Screen(val route: String) {
    object Register : Screen("register")   // Route for RegisterScreen
    object Login : Screen("login")         // Route for LoginScreen
    object Dashboard : Screen("dashboard")
    object AddReport: Screen("addreport") // Route for DashboardScreen
    object Viewreport : Screen("viewreport") // Route for DashboardScreen
    object Updatereport : Screen("updatereport") // Route for DashboardScreen

}
