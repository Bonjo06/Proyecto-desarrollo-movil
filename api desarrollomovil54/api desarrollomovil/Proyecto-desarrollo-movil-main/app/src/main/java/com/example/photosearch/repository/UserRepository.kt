package com.example.photosearch.repository

import android.content.Context
import com.example.photosearch.data.PhotoDatabase
import com.example.photosearch.data.UserEntity

class UserRepository(context: Context) {

    private val db = PhotoDatabase.getDatabase(context)
    private val userDao = db.userDao()

    // Registrar usuario con contraseña
    suspend fun registerUser(name: String, email: String, password: String) {
        val user = UserEntity(
            name = name,
            email = email,
            password = password
        )
        userDao.insertUser(user)
    }

    // Login
    suspend fun login(email: String, password: String): UserEntity? {
        return userDao.login(email, password)
    }

    // Obtener todos los usuarios (opcional)
    suspend fun getUser() = userDao.getUser()
}
