package com.example.photosearch.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("api/photos")
    suspend fun sendPhoto(@Body photo: PhotoRequest): PhotoRequest

    @GET("api/photos")
    suspend fun getPhotos(): List<PhotoResponse>

    // 👇 AGREGA ESTO: Método para ACTUALIZAR (Put)
    @PUT("api/photos/{id}")
    suspend fun updatePhoto(
        @Path("id") id: Int,
        @Body photo: PhotoRequest
    ): PhotoResponse

    // 👇 AGREGA ESTO: Método para BORRAR (Delete)
    @DELETE("api/photos/{id}")
    suspend fun deletePhoto(
        @Path("id") id: Int
    ): Response<Unit>
}