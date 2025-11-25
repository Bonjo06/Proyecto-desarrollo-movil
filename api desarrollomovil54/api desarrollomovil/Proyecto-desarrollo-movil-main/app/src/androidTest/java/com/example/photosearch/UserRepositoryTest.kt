package com.example.photosearch

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.room.Room
import com.example.photosearch.data.PhotoDatabase
import com.example.photosearch.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private lateinit var db: PhotoDatabase
    private lateinit var repo: UserRepository
    private lateinit var app: Application

    @Before
    fun setup() {
        app = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(
            app,
            PhotoDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        // ✔ Usuario correcto: UserRepository SOLO recibe userDao()
        repo = UserRepository(db.userDao() as Context)
    }

    @Test
    fun registerUser_savesUserCorrectly() = runBlocking {
        repo.registerUser("Bryan", "bryan@gmail.com", "1234")

        val user = repo.login("bryan@gmail.com", "1234")

        assertNotNull(user)
        assertEquals("Bryan", user?.name)
    }

    @Test
    fun login_returnsNullWhenCredentialsInvalid() = runBlocking {
        val user = repo.login("nada@nada.com", "wrongpass")
        assertNull(user)
    }
}
