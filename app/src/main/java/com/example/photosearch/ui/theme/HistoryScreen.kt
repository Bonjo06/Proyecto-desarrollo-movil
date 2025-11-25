package com.example.photosearch.ui.theme

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import coil.compose.AsyncImage
import com.example.photosearch.data.PhotoEntity
import com.example.photosearch.repository.PhotoRepository
import com.example.photosearch.api.PhotoResponse
import androidx.core.net.toUri

@Composable
fun HistoryScreen(photoList: List<PhotoEntity>, onBack: () -> Unit) {
    val context = LocalContext.current
    val repo = remember { PhotoRepository(context) }

    // 🔥 Fotos desde el backend
    var apiPhotos by remember { mutableStateOf<List<PhotoResponse>>(emptyList()) }

    // Cargar fotos del backend cuando se abre la pantalla
    LaunchedEffect(true) {
        apiPhotos = repo.getPhotosFromApi()
    }

    // 🔥 Combinar Room + Backend
    val allPhotos = remember(photoList, apiPhotos) {
        val localConverted = photoList.map {
            PhotoResponse(it.label, it.address, it.imagePath)
        }
        localConverted + apiPhotos
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("📸 Historial de Detecciones", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Volver a la cámara")
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(allPhotos) { photo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = photo.imagePath,
                            contentDescription = "Foto guardada",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(photo.label, style = MaterialTheme.typography.titleMedium)
                        Text("Dirección: ${photo.address}", style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botón Google Maps
                        Button(
                            onClick = {
                                try {
                                    val encodedAddress = Uri.encode(photo.address)
                                    val mapIntent = Intent(
                                        Intent.ACTION_VIEW,
                                        "https://www.google.com/maps/search/?api=1&query=$encodedAddress".toUri()
                                    )
                                    mapIntent.setPackage("com.google.android.apps.maps")
                                    context.startActivity(mapIntent)
                                } catch (_: Exception) {
                                    Toast.makeText(context, "No se pudo abrir Google Maps", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("Ver ubicación en mapa 🌍", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }
    }
}
