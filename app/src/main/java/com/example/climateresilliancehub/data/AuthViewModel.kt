package com.example.climateresiliencehub.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

import com.example.climateresilliancehub.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase // ✅ Important import

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference // ✅ Initialize reference

    var isLoading = mutableStateOf(false)
    var authError = mutableStateOf("")
    var currentUser = mutableStateOf<User?>(null)

    fun register(username: String, email: String, password: String, onSuccess: () -> Unit) {
        isLoading.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    val user = User(username = username, email = email, uid = uid)
                    database.child("users").child(uid).setValue(user)
                        .addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                currentUser.value = user
                                onSuccess()
                            } else {
                                authError.value = dbTask.exception?.message ?: "Database error"
                            }
                        }
                } else {
                    authError.value = task.exception?.message ?: "Registration failed"
                }
            }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        isLoading.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    database.child("users").child(uid).get()
                        .addOnSuccessListener { snapshot ->
                            currentUser.value = snapshot.getValue(User::class.java)
                            onSuccess()
                        }
                        .addOnFailureListener {
                            authError.value = it.message ?: "Failed to fetch user"
                        }
                } else {
                    authError.value = task.exception?.message ?: "Login failed"
                }
            }
    }

    fun logout() {
        auth.signOut()
        currentUser.value = null
    }
}
