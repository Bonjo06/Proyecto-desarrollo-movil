package com.example.photosearch.repository

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

object LocationHelper {
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): Location? {
        // Verificar permisos antes de intentar nada
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return null
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        return try {
            // 1. Intentamos obtener la última ubicación conocida (es rápido)
            var location = fusedLocationClient.lastLocation.await()

            // 2. Si es nula (el problema que tenías), forzamos una búsqueda de ubicación fresca
            if (location == null) {
                location = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).await()
            }
            location
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}