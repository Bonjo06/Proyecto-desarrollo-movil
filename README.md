# 📸 PhotoSearch – Proyecto de Desarrollo Móvil

## 🧩 Descripción del Proyecto
**PhotoSearch** es una aplicación móvil desarrollada en **Kotlin con Jetpack Compose (Material 3)**.  
Permite **capturar imágenes con la cámara**, **detectar objetos automáticamente usando ML Kit**, y **guardar los datos en una base de datos local (Room)** junto con la **ubicación GPS del usuario**.  

El sistema cuenta con un flujo completo de registro, almacenamiento local, historial de detecciones, y una interfaz moderna adaptada al diseño **Material Design 3**.

---

## 👥 Integrantes del Proyecto
| Nombre | Rol |
|--------|------|
| **Bryan Saavedra** | Desarrollador principal / Integración de cámara, Room y ML Kit |
| **Benjamín Mella** | Soporte de UI / Validación y navegación |

---

## ⚙️ Tecnologías Utilizadas
- **Lenguaje:Kotlin  
- **Framework: Jetpack Compose (Material 3)  
- **Arquitectura: MVVM (Model-View-ViewModel)  
- **Base de datos local:Room  
- **Reconocimiento de objetos: Google ML Kit  
- **Cámara:** CameraX API  
- **Ubicación:** Fused Location Provider (GPS)  
- **Gestión de estado:** ViewModel + StateFlow  
- **IDE:** Android Studio

---

## 🧠 Funcionalidades Implementadas

| Funcionalidad y Descripción |

| 🧾 **Registro validado** Verifica que el usuario ingrese nombre y apellido, y un correo válido (`@gmail.com` o `@duocuc.cl`). |
| 🏠 **Pantalla principal (Home)** Permite acceder a la cámara o al historial. |
| 📷 **Captura de fotos (CameraX)** Usa la cámara nativa del dispositivo para tomar fotos. |
| 🤖 **Detección de objetos (ML Kit)** Identifica objetos automáticamente en la imagen capturada. |
| 🌍 **Ubicación GPS** Registra la dirección física donde se tomó la foto. |
| 🖼️ **Historial de detecciones** Muestra las fotos guardadas, con su dirección, fecha y descripción. |
| 💾 **Base de datos local (Room)** | Guarda usuarios y fotos en una base SQLite local. |
| ✨ **Diseño moderno (Material 3)**  Interfaz visual limpia, adaptada a temas claros y oscuros. |
| 🔙 **Botón de volver**  Permite regresar fácilmente desde la cámara a la pantalla principal. |

---

## 🚀 Pasos para Ejecutar el Proyecto

### 1️⃣ Clonar el repositorio
```bash
git clone https://github.com/bryansaavedra25/proyecto-desarrollo-movil.git
Abrir en Android Studio

Abre Android Studio.

Selecciona “Open an existing project”.

Elige la carpeta del proyecto clonado.

3️⃣ Configurar SDK y librerías

Asegúrate de tener Android SDK 33 o superior.

Verifica en:
File → Project Structure → SDK Location

Si faltan dependencias, ejecuta:

gradlew build

4️⃣ Ejecutar la aplicación

Conecta tu dispositivo físico o usa un emulador Pixel (API 33 o superior).

Haz clic en Run ▶️ para compilar y ejecutar la app.

5️⃣ Conceder permisos

La aplicación solicita los siguientes permisos al iniciar:

📷 Cámara

📍 Ubicación (GPS)

Asegúrate de aceptarlos para el correcto funcionamiento.

🧭 Estructura del Proyecto
app/
 ├─ src/main/java/com/example/photosearch/
 │   ├─ data/          → Entidades, DAO y Base de datos Room
 │   ├─ repository/    → Lógica de datos y control de almacenamiento
 │   ├─ ui/theme/      → Pantallas Compose (Home, Register, Camera, History)
 │   ├─ viewmodel/     → Lógica de negocio y flujo de estado
 │   └─ MainActivity.kt → Navegación principal
 ├─ res/
 │   ├─ drawable/      → Iconos y recursos gráficos
 │   ├─ values/        → Temas, colores y tipografías
 └─ build.gradle

🏫 Información Académica

Institución: Duoc UC – Escuela de Informática y Telecomunicaciones

Asignatura: Desarrollo de Aplicaciones Móviles

Sección: 003D
