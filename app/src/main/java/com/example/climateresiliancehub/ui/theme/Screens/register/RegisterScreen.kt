package com.example.climateresiliencehub.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.climateresiliencehub.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = viewModel(),
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Register", style = MaterialTheme.typography.headlineLarge)

        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Spacer(Modifier.height(8.dp))
        TextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") })
        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            if (password == confirmPassword) {
                viewModel.register(email, password, onRegisterSuccess)
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Register")
        }

        TextButton(onClick = onLoginClick) {
            Text("Already have an account? Login")
        }

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
