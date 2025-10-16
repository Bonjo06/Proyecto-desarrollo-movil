package com.example.photosearch.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import java.util.Locale

class PhotoRepository(private val context: Context){

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentAddress(): String {
        return try {
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).await()
                ?: return "Ubicación desconocida (null)"
            if (!Geocoder.isPresent()) return "Servicio Geocoder no disponible"
            val geocoder = Geocoder(context, Locale.getDefault())

            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

            if (addresses != null && addresses.isNotEmpty()) {
                addresses[0].getAddressLine(0) ?: "Dirección no encontrada"
            } else {
                "Dirección no encontrada"
            }
        } catch (e: Exception) {
            "No se pudo obtener la dirección. Verifica tu conexión."
        }
    }

}