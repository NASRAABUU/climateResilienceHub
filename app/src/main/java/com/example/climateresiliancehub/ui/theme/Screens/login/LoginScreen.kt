package com.example.climateresiliencehub.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.climateresiliencehub.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Login", style = MaterialTheme.typography.headlineLarge)

        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            viewModel.login(email, password, onLoginSuccess)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Login")
        }

        TextButton(onClick = onRegisterClick) {
            Text("Don’t have an account? Register")
        }

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
