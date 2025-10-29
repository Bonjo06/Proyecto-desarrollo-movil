package com.example.photosearch.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.photosearch.data.PhotoEntity
import com.example.photosearch.repository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PhotoRepository(application.applicationContext)
    private val _photoList = MutableStateFlow<List<PhotoEntity>>(emptyList())
    val photoList: StateFlow<List<PhotoEntity>> = _photoList

    init {
        loadPhotos()
    }

    fun loadPhotos() {
        viewModelScope.launch {
            try {
                _photoList.value = repository.getAllPhotos()
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error al cargar fotos de DB", e)
            }
        }
    }

    fun onPhotoButtonPressed(label: String, imagePath: String = "sin_ruta") {
        viewModelScope.launch {
            val address = repository.getCurrentAddress()

            // Solo guarda si hay un label válido
            if (label.isNotBlank()) {
                repository.savePhoto(label, address, imagePath)
                Log.d("MainViewModel", "✅ Foto guardada: $label")
                loadPhotos()
            } else {
                Log.d("MainViewModel", "❌ Foto no guardada: etiqueta vacía")
            }
        }
    }
}
