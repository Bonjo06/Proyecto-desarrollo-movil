package com.example.photosearch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PhotoEntity::class, UserEntity::class], // Agregamos UserEntity
    version = 2,
    exportSchema = false
)
abstract class PhotoDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao
    abstract fun userDao(): UserDao // Nuevo DAO para usuarios

    companion object {
        @Volatile
        private var INSTANCE: PhotoDatabase? = null

        fun getDatabase(context: Context): PhotoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhotoDatabase::class.java,
                    "photo_db"
                )
                    .fallbackToDestructiveMigration() // Evita crashes en migración
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

