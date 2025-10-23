package com.example.photosearch.repository

import android.content.Context
import com.example.photosearch.data.PhotoDatabase
import com.example.photosearch.data.UserEntity

class UserRepository(context: Context) {

    private val db = PhotoDatabase.getDatabase(context)
    private val userDao = db.userDao()

    suspend fun registerUser(name: String, email: String) {
        userDao.insertUser(UserEntity(name = name, email = email))
    }

    suspend fun getUser() = userDao.getUser()
}
