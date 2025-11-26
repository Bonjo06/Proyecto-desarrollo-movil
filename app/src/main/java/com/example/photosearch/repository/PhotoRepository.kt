package com.example.photosearch.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import com.example.photosearch.api.PhotoRequest
import com.example.photosearch.api.RetrofitInstance
import com.example.photosearch.data.PhotoDatabase
import com.example.photosearch.data.PhotoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume

@Suppress("DEPRECATION")
class PhotoRepository(
    private val context: Context,
    private val photoDao: com.example.photosearch.data.PhotoDao =
        PhotoDatabase.getDatabase(context).photoDao()
) {

    @SuppressLint("MissingPermission")
    suspend fun getCurrentAddress(): String = withContext(Dispatchers.IO) {
        try {
            val location = LocationHelper.getCurrentLocation(context)
                ?: return@withContext "No se pudo obtener la ubicación actual."

            val geocoder = Geocoder(context, Locale.getDefault())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { cont ->
                    geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses ->
                        val result = if (addresses.isNotEmpty()) {
                            addresses[0].getAddressLine(0) ?: "Dirección no encontrada"
                        } else {
                            "No se encontró una dirección válida."
                        }
                        cont.resume(result)
                    }
                }
            } else {
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    addresses[0].getAddressLine(0) ?: "Dirección no encontrada"
                } else {
                    "No se encontró una dirección válida."
                }
            }
        } catch (e: SecurityException) {
            Log.e("PhotoRepository", "Error de permisos", e)
            "Permiso de ubicación denegado."
        } catch (e: Exception) {
            Log.e("PhotoRepository", "Error obteniendo dirección", e)
            "Error obteniendo la dirección. Verifica tu conexión a Internet."
        }
    }

    suspend fun savePhoto(label: String, address: String, imagePath: String) {
        withContext(Dispatchers.IO) {
            val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            val photo = PhotoEntity(label = label, address = address, date = date, imagePath = imagePath)
            photoDao.insert(photo)
        }
    }

    suspend fun getAllPhotos(): List<PhotoEntity> = withContext(Dispatchers.IO) {
        photoDao.getAll()
    }

    suspend fun sendPhotoToApi(label: String, address: String, imagePath: String) {
        withContext(Dispatchers.IO) {
            try {
                val request = PhotoRequest(label, address, imagePath)
                RetrofitInstance.api.sendPhoto(request)
                Log.d("PhotoRepository", "Foto enviada correctamente al backend")
            } catch (e: Exception) {
                Log.e("PhotoRepository", "Error enviando foto a la API: ${e.message}")
            }
        }
    }

    suspend fun getPhotosFromApi(): List<com.example.photosearch.api.PhotoResponse> {
        return try {
            RetrofitInstance.api.getPhotos()
        } catch (e: Exception) {
            Log.e("PhotoRepository", "Error obteniendo fotos del backend: ${e.message}")
            emptyList()
        }
    }

    // 👇 1. FUNCIÓN NUEVA: ACTUALIZAR (PUT)
    suspend fun updatePhotoRemote(id: Int, label: String, address: String, imagePath: String) {
        withContext(Dispatchers.IO) {
            try {
                val request = PhotoRequest(label, address, imagePath)
                RetrofitInstance.api.updatePhoto(id, request)
                Log.d("API", "Foto actualizada (ID: $id) correctamente")
            } catch (e: Exception) {
                Log.e("API", "Error al actualizar: ${e.message}")
            }
        }
    }

    // 👇 2. FUNCIÓN NUEVA: BORRAR (DELETE)
    suspend fun deletePhotoRemote(id: Int) {
        withContext(Dispatchers.IO) {
            try {
                RetrofitInstance.api.deletePhoto(id)
                Log.d("API", "Foto eliminada (ID: $id) correctamente")
            } catch (e: Exception) {
                Log.e("API", "Error al eliminar: ${e.message}")
            }
        }
    }
}