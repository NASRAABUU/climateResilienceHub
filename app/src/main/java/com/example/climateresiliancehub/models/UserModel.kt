package com.example.climateresiliancehub.models

import com.google.firebase.Timestamp


data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val createdAt: Timestamp = Timestamp.now()
)