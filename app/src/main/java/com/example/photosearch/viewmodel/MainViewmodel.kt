package com.example.photosearch.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.photosearch.repository.PhotoRepository
import kotlinx.coroutines.launch

// Fíjate en estos dos cambios importantes respecto a tu clase vacía:
// 1. Le añadimos un constructor: (application: Application)
// 2. Hacemos que herede de AndroidViewModel: : AndroidViewModel(application)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    // Todo este código va DENTRO de las llaves { } de la clase

    // Instancia del repositorio para obtener los datos.
    private val repository = PhotoRepository(application.applicationContext)

    // LiveData privado que solo el ViewModel puede modificar.
    private val _photoResult = MutableLiveData<String>()

    // LiveData público que la Vista (MainActivity) observará para recibir actualizaciones.
    val photoResult: LiveData<String> = _photoResult

    /**
     * Esta es la función que la Vista llamará cuando el usuario presione el botón.
     */
    fun onPhotoButtonPressed(objectLabel: String) {
        // Usamos viewModelScope para ejecutar la tarea en un hilo secundario de forma segura.
        viewModelScope.launch {
            // 1. Pide la dirección al repositorio.
            val address = repository.getCurrentAddress()
            val result = "Objeto: $objectLabel\nDirección: $address"

            // 2. Actualiza el LiveData con el resultado.
            _photoResult.postValue(result)
        }
    }
}