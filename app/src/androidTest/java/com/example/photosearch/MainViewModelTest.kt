package com.example.photosearch

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.photosearch.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewModelTest {

    private lateinit var app: Application
    private lateinit var vm: MainViewModel

    @Before
    fun setup() {
        app = ApplicationProvider.getApplicationContext()
        vm = MainViewModel(app)
    }

    @Test
    fun onPhotoButtonPressed_addsPhoto() = runBlocking {
        vm.onPhotoButtonPressed("Arbol", "/tmp/arbol.png")

        delay(300) // Esperar corrutina

        val list = vm.photoList.value

        assertEquals(1, list.size)
        assertEquals("Arbol", list[0].label)
    }
}
