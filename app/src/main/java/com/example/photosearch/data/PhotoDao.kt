package com.example.photosearch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PhotoDao {
    @Insert
    suspend fun insert(photo: PhotoEntity)

    @Query("SELECT * FROM photos ORDER BY id DESC")
    suspend fun getAll(): List<PhotoEntity>
}
