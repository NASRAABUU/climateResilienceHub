package com.example.climateresiliencehub.cloudinary

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File

data class CloudinaryResponse(
    val publicId: String,
    val url: String,
    val secureUrl: String
)

object CloudinaryApi {
    private const val CLOUD_NAME = "your_cloud_name" // replace with your Cloudinary cloud name
    private const val UPLOAD_PRESET = "your_unsigned_preset" // replace with your unsigned preset
    private const val UPLOAD_URL = "https://api.cloudinary.com/v1_1/$CLOUD_NAME/image/upload"

    private val client = OkHttpClient()

    fun uploadImage(
        imageFile: File,
        onResult: (CloudinaryResponse?) -> Unit
    ) {
        // ✅ Correct way to create RequestBody in OkHttp 4.x
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                imageFile.name,
                imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            )
            .addFormDataPart("upload_preset", UPLOAD_PRESET)
            .build()

        val request = Request.Builder()
            .url(UPLOAD_URL)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                e.printStackTrace()
                onResult(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        onResult(null)
                    } else {
                        val responseBody = it.body?.string()
                        if (responseBody != null) {
                            try {
                                val json = JSONObject(responseBody)
                                val cloudResponse = CloudinaryResponse(
                                    publicId = json.getString("public_id"),
                                    url = json.getString("url"),
                                    secureUrl = json.getString("secure_url")
                                )
                                onResult(cloudResponse)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                onResult(null)
                            }
                        } else {
                            onResult(null)
                        }
                    }
                }
            }
        })
    }
}
