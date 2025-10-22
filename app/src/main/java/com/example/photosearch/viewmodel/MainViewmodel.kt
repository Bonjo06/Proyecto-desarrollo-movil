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

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // 1. El repository ya está en el hilo principal aquí, pero sus funciones son suspend.
    private val repository = PhotoRepository(application.applicationContext)

    private val _photoList = MutableStateFlow<List<PhotoEntity>>(emptyList())
    val photoList: StateFlow<List<PhotoEntity>> = _photoList

    init {
        // 2. Ejecutar la carga de forma asíncrona es correcto, pero debe ser el primer lugar
        //    donde se llama a la DB para garantizar el Threading.
        loadPhotos()
    }

    /** 🔹 Carga todas las fotos guardadas */
    fun loadPhotos() {
        viewModelScope.launch {
            try {
                // El getAllPhotos() del Repository ya usa withContext(Dispatchers.IO)
                _photoList.value = repository.getAllPhotos()
            } catch (e: Exception) {
                // Manejo de error de DB
                Log.e("MainViewModel", "Error al cargar fotos de DB", e)
                // Opcional: Mostrar un Toast al usuario
            }
        }
    }

    /** 🔹 Guarda una foto con la etiqueta detectada y la ruta (por ahora simulada o vacía) */
    fun onPhotoButtonPressed(label: String, imagePath: String = "sin_ruta") {
        viewModelScope.launch {
            // El problema podría estar aquí, ya que getCurrentAddress hace mucho trabajo en IO
            val address = repository.getCurrentAddress()
            repository.savePhoto(label, address, imagePath)
            loadPhotos() // Recarga la lista de historial
        }
    }
}
