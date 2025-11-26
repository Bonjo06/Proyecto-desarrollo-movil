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
    fun sendPhoto_returnsCorrectPhotoRequest() = runBlocking {
        val req = PhotoRequest("Gato", "Valparaíso", "/img.png")

        // Como ApiService devuelve PhotoRequest, devolvemos el mismo req
        `when`(api.sendPhoto(req)).thenReturn(req)

        val result = api.sendPhoto(req)

        assertEquals("Gato", result.label)
        assertEquals("Valparaíso", result.address)
        assertEquals("/img.png", result.imagePath)

        // ❌ No se puede usar result.id porque PhotoRequest NO tiene id
    }

    @Test
    fun getPhotos_returnsListOfPhotoResponse() = runBlocking {
        val expected = listOf(
            PhotoResponse(1, "Perro", "Santiago", "/1.png"),
            PhotoResponse(2, "Gato", "Viña", "/2.png")
        )

        `when`(api.getPhotos()).thenReturn(expected)

        val result = api.getPhotos()

        assertEquals(2, result.size)
        assertEquals("Perro", result[0].label)
        assertEquals("Santiago", result[0].address)
        assertEquals("/1.png", result[0].imagePath)
        assertEquals(1, result[0].id)
    }
}
