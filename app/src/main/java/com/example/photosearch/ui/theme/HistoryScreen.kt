package com.example.photosearch.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.photosearch.data.PhotoEntity

@Composable
fun HistoryScreen(photoList: List<PhotoEntity>, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("📸 Historial de Detecciones", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Volver a la cámara")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(photoList) { photo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Objeto: ${photo.label}", style = MaterialTheme.typography.bodyLarge)
                        Text("Dirección: ${photo.address}", style = MaterialTheme.typography.bodyMedium)
                        Text("Fecha: ${photo.date}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

