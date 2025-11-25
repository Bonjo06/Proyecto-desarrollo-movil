package com.example.photosearch.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("api/photos")
    suspend fun sendPhoto(@Body photo: PhotoRequest): PhotoRequest

    @GET("api/photos")
    suspend fun getPhotos(): List<PhotoResponse>
}
