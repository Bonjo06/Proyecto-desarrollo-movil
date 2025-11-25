package com.example.photosearch

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.photosearch.data.PhotoDatabase
import com.example.photosearch.repository.PhotoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhotoRepositoryTest {

    private lateinit var db: PhotoDatabase
    private lateinit var repo: PhotoRepository
    private lateinit var app: Application

    @Before
    fun setup() {
        app = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(
            app,
            PhotoDatabase::class.java
        ).allowMainThreadQueries().build()

        repo = PhotoRepository(
            context = app,
            photoDao = db.photoDao()
        )
    }

    @Test
    fun savePhoto_savesCorrectly() = runBlocking {
        repo.savePhoto(
            label = "Perro",
            address = "Santiago",
            imagePath = "/tmp/perro.png"
        )

        val allPhotos = repo.getAllPhotos()

        assertEquals(1, allPhotos.size)
        assertEquals("Perro", allPhotos[0].label)
        assertEquals("Santiago", allPhotos[0].address)
        assertEquals("/tmp/perro.png", allPhotos[0].imagePath)
    }

    @Test
    fun getAllPhotos_returnsEmptyListInitially() = runBlocking {
        val list = repo.getAllPhotos()
        assertEquals(0, list.size)
    }
}
