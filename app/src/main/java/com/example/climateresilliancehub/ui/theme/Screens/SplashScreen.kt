package com.example.climateresilliancehub.ui.theme.Screens



import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


import kotlinx.coroutines.delay
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

import com.example.climateresilliancehub.R
import com.example.climateresilliancehub.navigation.Screen


@Composable
fun SplashScreen(navController: NavController) {
    // Navigate to Register screen after 2 seconds
    LaunchedEffect(key1 = true) {
        delay(2000L)
        navController.navigate(Screen.Register.route) {
            popUpTo(Screen.Register.route) { inclusive = true } // remove splash from backstack
        }
    }

    // UI for splash screen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Climate Resilience Hub Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Climate Resilience Hub", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}
