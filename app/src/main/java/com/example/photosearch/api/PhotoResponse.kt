package com.example.photosearch.api

data class PhotoResponse(
    val id: Int? = null,  // 👈 Necesario para editar/borrar
    val label: String,
    val address: String,
    val imagePath: String
)