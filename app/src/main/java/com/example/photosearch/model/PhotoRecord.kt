package com.example.photosearch.model

import android.graphics.Bitmap

data class PhotoRecord(
    val image: Bitmap,
    val objectLabel: String,
    val address: String
)
