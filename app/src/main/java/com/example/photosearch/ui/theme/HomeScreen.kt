package com.example.photosearch.ui.theme

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.photosearch.data.UserEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: UserEntity,
    onOpenCamera: () -> Unit,
    onOpenHistory: () -> Unit,  // 🔹 Nuevo parámetro para abrir historial
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bienvenido, ${user.name}") },
                actions = {
                    Button(
                        onClick = {
                            Toast.makeText(context, "Cerrando sesión...", Toast.LENGTH_SHORT).show()
                            onLogout()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cerrar sesión")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Selecciona una opción:",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 📷 Botón abrir cámara
            Button(
                onClick = onOpenCamera,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("📷 Abrir cámara", color = MaterialTheme.colorScheme.onPrimary)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🖼 Botón abrir historial
            Button(
                onClick = {
                    Toast.makeText(
                        context,
                        "Abriendo historial de detecciones 📸",
                        Toast.LENGTH_SHORT
                    ).show()
                    onOpenHistory() // ✅ Llama al callback
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("🖼 Ver historial de detecciones", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
