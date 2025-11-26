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

    private val repository = PhotoRepository(application.applicationContext)
    private val _photoList = MutableStateFlow<List<PhotoEntity>>(emptyList())
    val photoList: StateFlow<List<PhotoEntity>> = _photoList

    init {
        loadPhotos()
    }

    fun loadPhotos() {
        viewModelScope.launch {
            try {
                // Aquí podrías mezclar datos locales y remotos si quisieras
                _photoList.value = repository.getAllPhotos()
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error al cargar fotos de DB", e)
            }
        }
    }

    fun onPhotoButtonPressed(label: String, imagePath: String = "sin_ruta") {
        viewModelScope.launch {
            val address = repository.getCurrentAddress()

            if (label.isNotBlank()) {
                // 1. Guardar localmente
                repository.savePhoto(label, address, imagePath)

                // 2. Enviar a la API (Spring Boot)
                repository.sendPhotoToApi(label, address, imagePath)

                Log.d("MainViewModel", "✅ Foto guardada y enviada: $label")
                loadPhotos()
            } else {
                Log.d("MainViewModel", "❌ Foto no guardada: etiqueta vacía")
            }
        }
    }

    // 👇 FUNCIÓN NUEVA: ACTUALIZAR
    fun updatePhoto(id: Int, label: String, address: String, imagePath: String) {
        viewModelScope.launch {
            // Llamamos al repositorio para que hable con la API
            repository.updatePhotoRemote(id, label, address, imagePath)

            // Recargamos la lista para ver si hubo cambios (opcional si sincronizas)
            loadPhotos()
        }
    }

    // 👇 FUNCIÓN NUEVA: BORRAR
    fun deletePhoto(id: Int) {
        viewModelScope.launch {
            // Llamamos al repositorio para borrar en la API
            repository.deletePhotoRemote(id)

            // Recargamos la lista
            loadPhotos()
        }
    }
}