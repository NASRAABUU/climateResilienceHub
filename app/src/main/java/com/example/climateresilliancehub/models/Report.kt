package com.example.climateresilliancehub.models


data class Report(
    val reportId: String = "",        // Unique ID for each report
    val date: String = "",        //
    val reporterId: String = "",      // UID of the user who submitted the report
    val title: String = "",           // Short title for the report
    val description: String = "",     // Detailed description
    val location: String = "",        // Location of the event/issue
    val imageUrl: String = "",        // Optional image URL stored in Firebase Storage
    val timestamp: Long = System.currentTimeMillis() // Time of report creation
)

