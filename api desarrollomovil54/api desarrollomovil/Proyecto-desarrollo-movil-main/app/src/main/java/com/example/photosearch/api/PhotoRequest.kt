package com.example.photosearch.api

data class PhotoRequest(
    val label: String,
    val address: String,
    val imagePath: String
)
