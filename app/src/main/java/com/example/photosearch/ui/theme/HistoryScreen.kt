package com.example.photosearch.ui.theme

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Map
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
import com.example.photosearch.viewmodel.MainViewModel
import androidx.core.net.toUri

@Composable
fun HistoryScreen(
    photoList: List<PhotoEntity>,
    onBack: () -> Unit,
    viewModel: MainViewModel // 👈 IMPORTANTE: Ahora recibimos el ViewModel
) {
    val context = LocalContext.current
    val repo = remember { PhotoRepository(context) }

    // 🔥 Fotos desde el backend
    var apiPhotos by remember { mutableStateOf<List<PhotoResponse>>(emptyList()) }

    // Variables para controlar la edición
    var showEditDialog by remember { mutableStateOf(false) }
    var photoToEdit by remember { mutableStateOf<PhotoResponse?>(null) }
    var newLabelText by remember { mutableStateOf("") }

    // Cargar fotos del backend
    LaunchedEffect(true) {
        try {
            apiPhotos = repo.getPhotosFromApi()
        } catch (_: Exception) {
            // Error de conexión silencioso
        }
    }

    // 🔥 Combinar Room + Backend y asegurar que tengan formato común
    val allPhotos = remember(photoList, apiPhotos) {
        val localConverted = photoList.map {
            // Mapeamos el ID local a la estructura de respuesta
            PhotoResponse(it.id, it.label, it.address, it.imagePath)
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
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = photo.imagePath,
                            contentDescription = "Foto",
                            modifier = Modifier.fillMaxWidth().height(200.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(photo.label, style = MaterialTheme.typography.titleMedium)
                        Text("📍 ${photo.address}", style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(12.dp))

                        // --- BARRA DE BOTONES ---
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // 1. Botón Mapa (Corregido)
                            IconButton(onClick = {
                                try {
                                    val uri = "geo:0,0?q=${Uri.encode(photo.address)}".toUri()
                                    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                                    mapIntent.setPackage("com.google.android.apps.maps")
                                    context.startActivity(mapIntent)
                                } catch (_: Exception) {
                                    Toast.makeText(context, "Instala Google Maps", Toast.LENGTH_SHORT).show()
                                }
                            }) {
                                Icon(Icons.Default.Map, "Mapa", tint = MaterialTheme.colorScheme.primary)
                            }

                            // 2. Botón Editar (Usa la función updatePhoto)
                            IconButton(onClick = {
                                photoToEdit = photo
                                newLabelText = photo.label
                                showEditDialog = true
                            }) {
                                Icon(Icons.Default.Edit, "Editar", tint = MaterialTheme.colorScheme.secondary)
                            }

                            // 3. Botón Eliminar (Usa la función deletePhoto)
                            IconButton(onClick = {
                                photo.id?.let { id ->
                                    viewModel.deletePhoto(id) // 👈 Aquí se usa la función, eliminando el error
                                    Toast.makeText(context, "Eliminando...", Toast.LENGTH_SHORT).show()
                                }
                            }) {
                                Icon(Icons.Default.Delete, "Borrar", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }

    // --- DIÁLOGO POPUP PARA EDITAR ---
    if (showEditDialog && photoToEdit != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar Etiqueta") },
            text = {
                OutlinedTextField(
                    value = newLabelText,
                    onValueChange = { newLabelText = it },
                    label = { Text("Nuevo nombre") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    photoToEdit?.let { photo ->
                        photo.id?.let { id ->
                            // 👈 Aquí se usa la función updatePhoto
                            viewModel.updatePhoto(id, newLabelText, photo.address, photo.imagePath)
                            Toast.makeText(context, "Actualizado", Toast.LENGTH_SHORT).show()
                        }
                    }
                    showEditDialog = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Button(onClick = { showEditDialog = false }) { Text("Cancelar") }
            }
        )
    }
}