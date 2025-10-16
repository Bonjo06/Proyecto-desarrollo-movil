package com.example.photosearch.model

import android.graphics.Bitmap

/**
 * Representa la información de una foto guardada. Es el "Qué".
 *
 * @param image La foto del objeto.
 * @param objectLabel El nombre del objeto identificado (ej. "Taza", "Libro").
 * @param address La dirección física donde se tomó la foto.
 */
data class PhotoRecord(
    val image: Bitmap,
    val objectLabel: String,
    val address: String
)
