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

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!showWelcome) {
            Text("Registro de Usuario", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = null 
                },
                label = { Text("Nombre completo") },
                isError = nameError != null,
                supportingText = {
                    nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            //correo electrónico 
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null 
                },
                label = { Text("Correo electrónico") },
                isError = emailError != null,
                supportingText = {
                    emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            //registrar
            Button(
                onClick = {
                    scope.launch {
                        val nombreValido = name.text.trim().split(" ").size >= 2
                        val correoValido = email.text.matches(Regex("^[A-Za-z0-9+_.-]+@(gmail\\.com|duocuc\\.cl)$"))

                        when {
                            !nombreValido -> nameError = "Debe ingresar nombre y apellido"
                            !correoValido -> emailError = "Debe usar un correo válido (@gmail.com o @duocuc.cl)"
                            else -> {
                                userRepo.registerUser(name.text, email.text)
                                Toast.makeText(
                                    context,
                                    "Usuario registrado correctamente ✅",
                                    Toast.LENGTH_SHORT
                                ).show()
                                showWelcome = true
                                delay(2000)
                                onRegisterDone()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Registrar", color = MaterialTheme.colorScheme.onPrimary)
            }
        }

        AnimatedVisibility(
            visible = showWelcome,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎉 Bienvenido, ${name.text} 👋", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(10.dp))
                Text("Preparando tu experiencia...", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


