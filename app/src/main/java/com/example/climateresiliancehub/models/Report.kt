package com.example.climateresiliencehub.model

// Represents a report document in Firestore
data class Report(
    val id: String = "",           // Firestore document ID
    val title: String = "",        // Report title
    val description: String = "",  // Report description
    val imageUrl: String = ""      // Cloudinary/Firebase Storage image URL
)
