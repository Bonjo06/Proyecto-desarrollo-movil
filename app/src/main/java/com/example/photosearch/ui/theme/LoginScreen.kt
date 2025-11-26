package com.example.photosearch.ui.theme

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.photosearch.data.UserEntity
import com.example.photosearch.repository.UserRepository
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: (UserEntity) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userRepo = remember { UserRepository(context) }

    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; isError = false },
            label = { Text("Correo electrónico") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("emailField"),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; isError = false },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("passwordField"),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = isError
        )

        if (isError) {
            Text(
                text = "Usuario o contraseña incorrectos",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Principal: ENTRAR
        Button(
            onClick = {
                scope.launch {
                    val user = userRepo.login(email.text, password.text)
                    if (user != null) {
                        Toast.makeText(context, "Bienvenido ${user.name}", Toast.LENGTH_SHORT).show()
                        onLoginSuccess(user)
                    } else {
                        isError = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("loginButton")
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(30.dp))
        Divider()
        Spacer(modifier = Modifier.height(10.dp))

        // BOTÓN ABAJO: IR A REGISTRO
        TextButton(
            onClick = onNavigateToRegister,
            modifier = Modifier.testTag("goToRegisterButton")
        ) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}
