package com.example.photosearch.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await
import java.util.Locale

class PhotoRepository(private val context: Context){

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission") // El permiso se verificará en la Activity/View
    suspend fun getCurrentAddress(): String {
        return try {
            // 1. Obtener coordenadas
            val location = fusedLocationClient.lastLocation.await()
                ?: return "Ubicación desconocida (null)"

            // 2. Convertir a dirección
            // RN-02 y RN-04: Depende de internet y es una aproximación.
            if (!Geocoder.isPresent()) return "Servicio Geocoder no disponible"
            val geocoder = Geocoder(context, Locale.getDefault())

            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

            if (addresses != null && addresses.isNotEmpty()) {
                addresses[0].getAddressLine(0) ?: "Dirección no encontrada"
            } else {
                "Dirección no encontrada"
            }
        } catch (e: Exception) {
            // Manejo de error si no hay internet o el servicio falla
            "No se pudo obtener la dirección. Verifica tu conexión."
        }
    }

}