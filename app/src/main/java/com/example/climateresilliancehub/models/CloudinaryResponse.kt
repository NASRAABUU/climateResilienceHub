package com.example.climateresilliancehub.network

import com.google.gson.annotations.SerializedName

data class CloudinaryResponse(
    val url: String,
    @SerializedName("secure_url") val secureUrl: String,
    val public_id: String
)


