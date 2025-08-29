package com.example.climateresiliencehub.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.climateresiliencehub.R
import com.example.climateresiliencehub.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: AuthViewModel = viewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToRegister: (() -> Unit)? = null
) {
    var navigate by remember { mutableStateOf(false) }

    // Simulate splash delay
    LaunchedEffect(Unit) {
        delay(2000) // 2 seconds splash
        navigate = true
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Display your logo from drawable
        Image(
            painter = painterResource(id = R.drawable.logo), // Replace 'logo' with your drawable name
            contentDescription = "App Logo",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
        )

        // Navigate after delay
        if (navigate) {
            onNavigateToLogin() // Or choose onNavigateToDashboard if user is logged in
        }
    }
}
