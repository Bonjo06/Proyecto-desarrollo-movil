package com.example.photosearch.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import com.example.photosearch.data.PhotoDatabase
import com.example.photosearch.data.PhotoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume


class PhotoRepository(private val context: Context) {

    private val photoDao = PhotoDatabase.getDatabase(context).photoDao()

    @SuppressLint("MissingPermission")
    suspend fun getCurrentAddress(): String = withContext(Dispatchers.IO) {
        try {
            val location = LocationHelper.getLastKnownLocation(context)
                ?: return@withContext "No se pudo obtener la ubicación actual."

            val geocoder = Geocoder(context, Locale.getDefault())

            // ✅ Usa el nuevo método asíncrono si Android >= 13
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
}
