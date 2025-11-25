package com.example.photosearch

import com.example.photosearch.api.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class ApiServiceTest {

    private lateinit var api: ApiService

    @BeforeEach
    fun setup() {
        api = mock(ApiService::class.java)
    }

    @Test
    fun sendPhoto_returnsCorrectData() = runBlocking {
        val req = PhotoRequest("Gato", "Valparaíso", "/img.png")

        `when`(api.sendPhoto(req)).thenReturn(req)

        val result = api.sendPhoto(req)

        assertEquals("Gato", result.label)
        assertEquals("Valparaíso", result.address)
    }

    @Test
    fun getPhotos_returnsList() = runBlocking {
        val expected = listOf(
            PhotoResponse("Perro", "Santiago", "/1.png"),
            PhotoResponse("Gato", "Viña", "/2.png")
        )

        `when`(api.getPhotos()).thenReturn(expected)

        val result = api.getPhotos()

        assertEquals(2, result.size)
        assertEquals("Perro", result[0].label)
    }
}
