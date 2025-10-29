package com.example.photosearch.ui.theme

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.photosearch.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(onRegisterDone: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userRepo = remember { UserRepository(context) }

    var name by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var showWelcome by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🧾 Título normal si no hay animación activa
        if (!showWelcome) {
            Text("Registro de Usuario", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    scope.launch {
                        if (name.text.isNotBlank() && email.text.isNotBlank()) {
                            userRepo.registerUser(name.text, email.text)
                            Toast.makeText(
                                context,
                                "Usuario registrado correctamente ✅",
                                Toast.LENGTH_SHORT
                            ).show()
                            showWelcome = true
                            delay(2000) // ⏱️ Espera 2 segundos antes de pasar al menú
                            onRegisterDone()
                        } else {
                            Toast.makeText(
                                context,
                                "Por favor completa todos los campos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Registrar", color = MaterialTheme.colorScheme.onPrimary)
            }
        }

        // 🎉 Animación de bienvenida
        AnimatedVisibility(
            visible = showWelcome,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎉 Bienvenido, ${name.text} 👋",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Preparando tu experiencia...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
