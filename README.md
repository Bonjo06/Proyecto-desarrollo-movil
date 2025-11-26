📸 PhotoSearch – Informe Técnico de Entrega

🧩 Descripción del Proyecto

PhotoSearch es una solución integral compuesta por una aplicación móvil (Android) y un backend (API REST).

La aplicación móvil, desarrollada en Kotlin con Jetpack Compose (Material 3), permite capturar imágenes, detectar objetos con ML Kit y guardar un historial local. Estos datos se sincronizan con un servicio web externo (API) desarrollado en Spring Boot para centralizar la información.

👥 Integrantes del Proyecto

Nombre

Rol

Bryan Saavedra

Backend (Spring Boot/Laragon) e Integración Retrofit

Benjamín Mella

Frontend (UI/UX), Cámara y Validaciones

⚙️ Tecnologías Utilizadas

📱 Aplicación Móvil (Frontend)

Lenguaje: Kotlin

Framework: Jetpack Compose (Material 3)

Arquitectura: MVVM (Model-View-ViewModel)

Base de Datos Local: Room (SQLite)

IA / ML: Google ML Kit (Object Detection)

Cámara: CameraX API

Ubicación: Fused Location Provider (GPS)

IDE: Android Studio Ladybug/Koala

☁️ Backend (API REST)

Framework: Spring Boot (Java)

IDE: Visual Studio Code

Base de Datos: MySQL / MariaDB (Gestionada con Laragon)

URL Base del Servidor: http://localhost:8080/api/photos

🧠 Funcionalidades Implementadas

Funcionalidad

Descripción

🧾 Registro validado

Validación de correo (@gmail.com, @duocuc.cl) y campos obligatorios.

📷 Captura Inteligente

Uso de CameraX para tomar fotos y ML Kit para detectar qué objeto es.

🌍 Geolocalización

Obtiene la dirección física (calle/ciudad) mediante GPS al momento de la foto.

💾 Persistencia Híbrida

Guarda en Room (local) para acceso offline y sincroniza con Spring Boot (remoto).

🔌 Consumo de API

Envío de datos (POST), consulta (GET), actualización (PUT) y eliminación (DELETE) al servidor local.

🖼️ Historial Unificado

Muestra detecciones locales y las traídas desde el servidor.

🚀 Pasos para Ejecutar el Proyecto

1️⃣ Configurar el Backend (API Spring Boot)

Para que la aplicación pueda enviar datos, el servidor debe estar corriendo localmente.

Abre Laragon e inicia los servicios (Botón "Iniciar Todo") para levantar la Base de Datos MySQL.

Abre la carpeta del proyecto backend en Visual Studio Code.

Ejecuta el proyecto Spring Boot (Run Java).

Verifica que la API responde en tu navegador o Postman:

http://localhost:8080/api/photos

2️⃣ Ejecutar la Aplicación Móvil

Abre la carpeta app en Android Studio.

Espera a que Gradle sincronice las dependencias.

Configuración de IP: La app está configurada para conectar al localhost de la computadora desde el emulador.

En RetrofitInstance.kt se usa la URL: http://10.0.2.2:8080/ (Esta IP es el puente del emulador hacia tu Laragon/Spring Boot).

Inicia el emulador (API 33 o superior).

Haz clic en Run 'app' ▶️.

3️⃣ Permisos Necesarios

Al iniciar por primera vez, acepta los permisos:

📷 Cámara: Para capturar imágenes.

📍 Ubicación: Para registrar la dirección de la detección.

📸 Evidencia de Entrega

1. APK Firmado y Llave

Se ha generado correctamente el archivo .jks y el APK firmado.


2. Planificación (Trello)

El equipo utilizó Trello para la distribución de tareas Backend/Frontend.
el link del trello :https://trello.com/b/7Jc0H0Dr/proyecto-de-desarrollo-moviles

🧭 Estructura del Proyecto (App)

app/
 ├─ src/main/java/com/example/photosearch/
 │   ├─ api/           → Interfaces Retrofit (GET, POST, PUT, DELETE)
 │   ├─ data/          → Entidades Room (User, Photo) y DAOs
 │   ├─ repository/    → Repositorio único (Maneja Room + API)
 │   ├─ ui/theme/      → Pantallas (Login, Camera, History)
 │   ├─ viewmodel/     → MainViewModel (Lógica de negocio)
 │   └─ MainActivity.kt
 └─ build.gradle.kts   → Configuración y Firmado (SigningConfigs)


🏫 Información Académica

Institución: Duoc UC – Escuela de Informática y Telecomunicaciones

Asignatura: Desarrollo de Aplicaciones Móviles (DSY1105)

Sección: 003D
