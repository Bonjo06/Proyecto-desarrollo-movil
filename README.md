📸 PhotoSearch – Informe Técnico de Entrega
🧩 Descripción del Proyecto

PhotoSearch es una solución integral compuesta por una aplicación móvil (Android) y un backend (API REST).
La aplicación móvil, desarrollada en Kotlin con Jetpack Compose (Material 3), permite capturar imágenes, detectar objetos con ML Kit y guardar un historial local. Estos datos se sincronizan con un servicio web externo (API) desarrollado en Spring Boot para centralizar la información.

👥 Integrantes del Proyecto
Nombre	Rol
Bryan Saavedra	Backend (Spring Boot/Laragon) e Integración Retrofit
Benjamín Mella	Frontend (UI/UX), Cámara y Validaciones
⚙️ Tecnologías Utilizadas
📱 Aplicación Móvil (Frontend)

Lenguaje: Kotlin

Framework: Jetpack Compose (Material 3)

Arquitectura: MVVM

Base de Datos Local: Room (SQLite)

IA / ML: Google ML Kit (Object Detection)

Cámara: CameraX API

Ubicación: Fused Location Provider

IDE: Android Studio Ladybug/Koala

☁️ Backend (API REST)

Framework: Spring Boot (Java)

IDE: Visual Studio Code

Base de Datos: MySQL/MariaDB (Laragon)

URL Base del Servidor: http://localhost:8080/api/photos

🧠 Funcionalidades Implementadas
Funcionalidad	Descripción
🧾 Registro validado	Validación de correo y campos obligatorios
📷 Captura Inteligente	CameraX + ML Kit para detectar objetos
🌍 Geolocalización	Obtención de dirección mediante GPS
💾 Persistencia Híbrida	Room local + API Spring Boot
🔌 Consumo de API	GET, POST, PUT, DELETE con Retrofit
🖼️ Historial	Lista combinada local + servidor
🚀 Pasos para Ejecutar el Proyecto
1️⃣ Configurar el Backend (Spring Boot)

Iniciar Laragon

Abrir el backend en VS Code

Ejecutar Spring Boot

Probar en navegador:

http://localhost:8080/api/photos

2️⃣ Ejecutar la Aplicación Móvil

Abrir Android Studio

Usar emulador (API 33+)

La app se conecta mediante:

http://10.0.2.2:8080/

3️⃣ Permisos Necesarios

Cámara

Ubicación

🖼️ Evidencia de Entrega
1. APK Firmado y Llave

Generado exitosamente (.jks + APK release)

2. Planificación (Trello)

Tablero del proyecto:
https://trello.com/b/7Jc0H0Dr/proyecto-de-desarrollo-moviles

🧭 Estructura del Proyecto (App)
app/
 ├─ api/           → Retrofit (POST, GET, PUT, DELETE)
 ├─ data/          → DAO + Entities (Room)
 ├─ repository/    → Lógica de datos (Room + API)
 ├─ ui/theme/      → Pantallas (Login, Camera, Historial)
 ├─ viewmodel/     → MainViewModel
 └─ MainActivity.kt

🧪 📌 Pruebas Unitarias Implementadas

Se desarrollaron pruebas unitarias para garantizar la correcta funcionalidad del sistema, especialmente en la comunicación con la API y en el uso de la base de datos Room.

Las pruebas aseguran:

Integridad de datos

Funcionamiento de la lógica del repositorio

Correcto comportamiento de los métodos Retrofit simulados

Verificación de credenciales en inicio de sesión

✅ 1. ApiServiceTest (Mock Retrofit)

📄 Valida que las llamadas GET y POST funcionen mediante simulación con Mockito.

class ApiServiceTest {

    private lateinit var api: ApiService

    @BeforeEach
    fun setup() {
        api = mock(ApiService::class.java)
    }

    @Test
    fun sendPhoto_returnsCorrectData() = runBlocking {
        val req = PhotoRequest("Gato", "Valparaíso", "/img.png")

        `when`(api.sendPhoto(req)).thenReturn(req)

        val result = api.sendPhoto(req)

        assertEquals("Gato", result.label)
        assertEquals("Valparaíso", result.address)
    }

    @Test
    fun getPhotos_returnsList() = runBlocking {
        val expected = listOf(
            PhotoResponse(1, "Perro", "Santiago", "/1.png"),
            PhotoResponse(2, "Gato", "Viña", "/2.png")
        )

        `when`(api.getPhotos()).thenReturn(expected)

        val result = api.getPhotos()

        assertEquals(2, result.size)
        assertEquals("Perro", result[0].label)
    }
}

✅ 2. UserRepositoryTest (Room Database en memoria)

📄 Prueba registro y login con base de datos in-memory.

class UserRepositoryTest {

    private lateinit var db: PhotoDatabase
    private lateinit var repo: UserRepository
    private lateinit var app: Application

    @Before
    fun setup() {
        app = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(
            app,
            PhotoDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        repo = UserRepository(app) // mantiene tu estructura original
    }

    @Test
    fun registerUser_savesUserCorrectly() = runBlocking {
        repo.registerUser("Bryan", "bryan@gmail.com", "1234")
        val user = repo.login("bryan@gmail.com", "1234")

        assertNotNull(user)
        assertEquals("Bryan", user?.name)
    }

    @Test
    fun login_returnsNullWhenCredentialsInvalid() = runBlocking {
        val user = repo.login("correo@noexiste.com", "pass")
        assertNull(user)
    }
}

✅ 3. UserDaoTest (Pruebas directas al DAO)

📄 Verifica inserción y login sin pasar por el repositorio.

class UserDaoTest {

    private lateinit var db: PhotoDatabase

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PhotoDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @Test
    fun insertAndLoginUser() = runBlocking {
        val dao = db.userDao()

        val user = UserEntity(
            name = "Test",
            email = "test@gmail.com",
            password = "1234"
        )

        dao.insertUser(user)

        val result = dao.login("test@gmail.com", "1234")

        assertNotNull(result)
        assertEquals("Test", result?.name)
    }
}

🧪 Herramientas usadas en Testing
Herramienta	Uso
JUnit 4/5	Ejecución de casos de prueba
Mockito	Mocking de API Retrofit
Room In-Memory	Base de datos virtual para testing
Coroutines + runBlocking	Pruebas de funciones suspend
🎯 Conclusión

Las pruebas unitarias permiten validar:

Correcto funcionamiento de las funciones CRUD de la API.

Validación del proceso de login y registro.

Integridad del almacenamiento local con Room.

Ejecución correcta de la lógica del repositorio.

Con esto, PhotoSearch demuestra un desarrollo robusto, mantenible y probado, cumpliendo los requisitos de la asignatura.
