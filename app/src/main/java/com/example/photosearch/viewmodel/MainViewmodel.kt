package com.example.photosearch.viewmodel

import android.app.Application
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

    /** 🔹 Carga todas las fotos guardadas */
    fun loadPhotos() {
        viewModelScope.launch {
            _photoList.value = repository.getAllPhotos()
        }
    }

    /** 🔹 Guarda una foto con la etiqueta detectada y la ruta (por ahora simulada o vacía) */
    fun onPhotoButtonPressed(label: String, imagePath: String = "sin_ruta") {
        viewModelScope.launch {
            val address = repository.getCurrentAddress()
            repository.savePhoto(label, address, imagePath)
            loadPhotos()
        }
    }
}
