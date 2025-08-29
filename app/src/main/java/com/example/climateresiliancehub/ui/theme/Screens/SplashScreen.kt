package com.example.climateresiliencehub.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import com.example.climateresiliencehub.viewmodel.AuthViewModel

@Composable
fun SplashScreen(
    viewModel: AuthViewModel = viewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    // State to trigger navigation
    var navigate by remember { mutableStateOf(false) }

    // Launch effect for splash delay
    LaunchedEffect(Unit) {
        delay(2000) // 2 seconds splash
        navigate = true
    }

    // Navigate after delay based on user login
    if (navigate) {
        if (viewModel.isUserLoggedIn()) {
            onNavigateToDashboard()
        } else {
            onNavigateToLogin()
        }
    }

    // UI of the splash screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D47A1)), // Deep blue background
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "🌍 Climate Resilience Hub",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator(color = Color.White)
        }
    }
}
