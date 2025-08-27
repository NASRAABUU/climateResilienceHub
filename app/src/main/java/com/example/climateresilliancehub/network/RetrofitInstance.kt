package com.example.climateresilliancehub.network



import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://api.cloudinary.com/"

    val cloudinaryApi: CloudinaryApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloudinaryApi::class.java)
    }
}
