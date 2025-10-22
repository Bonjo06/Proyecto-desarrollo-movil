package com.example.photosearch.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val label: String,
    val address: String,
    val date: String,
    val imagePath: String
)
