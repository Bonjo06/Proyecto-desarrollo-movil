package com.example.photosearch.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.photosearch.repository.PhotoRepository
import kotlinx.coroutines.launch
import java.net.URLEncoder

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PhotoRepository(application.applicationContext)
    private val _photoResult = MutableLiveData<String>()
    val photoResult: LiveData<String> = _photoResult
    fun onPhotoButtonPressed() {
        viewModelScope.launch {
            val address = repository.getCurrentAddress()

            _photoResult.postValue(address)
        }
    }
}