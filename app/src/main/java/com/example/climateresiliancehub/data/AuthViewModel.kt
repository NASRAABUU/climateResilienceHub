package com.example.climateresiliencehub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isAuthenticated = MutableStateFlow(auth.currentUser != null)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun register(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _isAuthenticated.value = true
                        onSuccess()
                    } else {
                        _errorMessage.value = task.exception?.message
                    }
                }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _isAuthenticated.value = true
                        onSuccess()
                    } else {
                        _errorMessage.value = task.exception?.message
                    }
                }
        }
    }
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null // Example for FirebaseAuth
    }

    fun logout() {
        auth.signOut()
        _isAuthenticated.value = false
    }
}
