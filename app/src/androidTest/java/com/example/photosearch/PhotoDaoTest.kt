package com.example.photosearch

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.photosearch.data.PhotoDao
import com.example.photosearch.data.PhotoDatabase
import com.example.photosearch.data.PhotoEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhotoDaoTest {

    private lateinit var dao: PhotoDao
    private lateinit var db: PhotoDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        db = Room.inMemoryDatabaseBuilder(context, PhotoDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = db.photoDao()
    }

    @Test
    fun insertPhoto_andRead() = runBlocking {
        val photo = PhotoEntity(
            label = "Gato",
            address = "Valparaíso",
            date = "20/11/2025 10:00",
            imagePath = "/tmp/gato.png"
        )

        dao.insert(photo)

        val result = dao.getAll()

        assertEquals(1, result.size)
        assertEquals("Gato", result[0].label)
    }
}