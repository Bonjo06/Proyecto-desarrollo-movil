Aquí tienes el informe **actualizado** incorporando los detalles técnicos del Backend (Spring Boot, Laragon, VS Code) y la configuración de la API que mencionaste.

Puedes copiar y pegar este contenido directamente en tu archivo `README.md`.

-----

# 📸 PhotoSearch – Informe Técnico de Entrega

## 🧩 Descripción del Proyecto

**PhotoSearch** es una solución integral compuesta por una **aplicación móvil (Android)** y un **backend (API REST)**.

La aplicación móvil, desarrollada en **Kotlin con Jetpack Compose (Material 3)**, permite **capturar imágenes**, **detectar objetos con ML Kit** y guardar un historial local. Estos datos se sincronizan con un servicio web externo (API) para centralizar la información.

-----

## 👥 Integrantes del Proyecto

| Nombre | Rol |
|--------|------|
| **Bryan Saavedra** | Backend (Spring Boot/Laragon) e Integración Retrofit |
| **Benjamín Mella** | Frontend (UI/UX), Cámara y Validaciones |

-----

## ⚙️ Stack Tecnológico

### 📱 Aplicación Móvil

  - **Lenguaje:** Kotlin
  - **Framework:** Jetpack Compose (Material 3)
  - **Arquitectura:** MVVM (Model-View-ViewModel)
  - **Base de Datos Local:** Room (SQLite)
  - **IA / ML:** Google ML Kit (Object Detection)
  - **Cámara:** CameraX API
  - **Ubicación:** Fused Location Provider (GPS)
  - **IDE:** Android Studio Ladybug/Koala

### ☁️ Backend (API REST)

  - **Framework:** Spring Boot (Java)
  - **IDE:** Visual Studio Code
  - **Base de Datos:** MySQL / MariaDB (Gestionada con **Laragon**)
  - **URL Base:** `http://localhost:8080/api/photos`

-----

## 🧠 Funcionalidades Implementadas

| Funcionalidad | Descripción |
|----------------|-------------|
| 🧾 **Registro validado** | Validación de correo (`@gmail.com`, `@duocuc.cl`) y campos obligatorios. |
| 📷 **Captura Inteligente** | Uso de CameraX para tomar fotos y ML Kit para detectar qué objeto es. |
| 🌍 **Geolocalización** | Obtiene la dirección física (calle/ciudad) mediante GPS al momento de la foto. |
| 💾 **Persistencia Híbrida** | Guarda en **Room (local)** para acceso offline y sincroniza con **Spring Boot (remoto)**. |
| 🔌 **Consumo de API** | Envío de datos (POST) y consulta de historial (GET) al servidor local. |
| 🖼️ **Historial Unificado** | Muestra detecciones locales y las traídas desde el servidor. |

-----

## 🚀 Pasos para Ejecutar el Proyecto

### 1️⃣ Configurar el Backend (API Spring Boot)

Para que la aplicación pueda enviar datos, el servidor debe estar corriendo.

1.  Abre **Laragon** e inicia los servicios (Botón "Iniciar Todo") para levantar la Base de Datos MySQL.
2.  Abre la carpeta del proyecto backend en **Visual Studio Code**.
3.  Ejecuta el proyecto Spring Boot.
4.  Verifica que la API responde en:
      * `http://localhost:8080/api/photos`

### 2️⃣ Ejecutar la Aplicación Móvil

1.  Clona el repositorio y abre la carpeta `app` en **Android Studio**.
2.  Espera a que Gradle sincronice las dependencias.
3.  Verifica la conexión a la API en `RetrofitInstance.kt`:
      * *Nota:* Para el emulador Android, `localhost` se mapea como `10.0.2.2`.
      * La URL configurada debe ser: `http://10.0.2.2:8080/`
4.  Conecta tu dispositivo o inicia el emulador.
5.  Haz clic en **Run ▶️**.

### 3️⃣ Permisos Necesarios

Al iniciar, la app solicitará:

  * 📷 **Cámara:** Para capturar imágenes.
  * 📍 **Ubicación:** Para registrar dónde se detectó el objeto.

-----

## 🧭 Estructura del Proyecto (App)

```text
app/
 ├─ src/main/java/com/example/photosearch/
 │   ├─ api/           → Interfaces Retrofit y Modelos de API
 │   ├─ data/          → Entidades Room (User, Photo) y DAOs
 │   ├─ repository/    → Repositorio único (Maneja Room + API)
 │   ├─ ui/theme/      → Pantallas (Login, Camera, History)
 │   ├─ viewmodel/     → MainViewModel (Lógica de negocio)
 │   └─ MainActivity.kt
 └─ build.gradle.kts   → Configuración y Firmado (SigningConfigs)
```

-----

## 🏫 Información Académica

  * **Institución:** Duoc UC – Escuela de Informática y Telecomunicaciones
  * **Asignatura:** Desarrollo de Aplicaciones Móviles (DSY1105)
  * **Sección:** 003D
