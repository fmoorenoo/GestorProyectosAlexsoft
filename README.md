<div align="center">

# Gestor de Proyectos · Alexsoft

### Aplicación de escritorio para la consulta y gestión de proyectos y tareas

**Kotlin Desktop · Interfaz Compose · Consumo de API REST**

![Kotlin](https://img.shields.io/badge/Kotlin-1A1B26?style=for-the-badge&logo=kotlin&logoColor=E46B79)
![Compose Desktop](https://img.shields.io/badge/Compose_Desktop-1A1B26?style=for-the-badge&logo=jetpackcompose&logoColor=E46B79)
![Ktor](https://img.shields.io/badge/Ktor_Client-1A1B26?style=for-the-badge&logo=ktor&logoColor=E46B79)
![Gradle](https://img.shields.io/badge/Gradle-1A1B26?style=for-the-badge&logo=gradle&logoColor=E46B79)

[![Estado](https://img.shields.io/badge/ESTADO-Proyecto_académico-712B39?style=flat-square)](#estado-del-proyecto)
[![Plataforma](https://img.shields.io/badge/PLATAFORMA-Desktop-22252C?style=flat-square)](#tecnologías)
[![API](https://img.shields.io/badge/REPOSITORIO-API_relacionada-22252C?style=flat-square)](https://github.com/fmoorenoo/metodos_alexsoftAPI)

</div>

---

## Sobre el proyecto

**Gestor de Proyectos · Alexsoft** es una aplicación de escritorio desarrollada con **Kotlin y Compose for Desktop** que consume una API REST para consultar y gestionar información de proyectos, tareas y programadores.

El proyecto se centra en la construcción de una interfaz gráfica, la navegación entre pantallas y la integración de peticiones HTTP con servicios externos. Forma parte de un desarrollo académico y depende de una API local para funcionar.

## Funcionalidades

- **Inicio de sesión:** pantalla de acceso y petición de autenticación al backend.
- **Proyectos activos:** listado de proyectos y filtro de proyectos asociados al gestor.
- **Historial:** pantalla dedicada a consultar proyectos finalizados.
- **Detalle de proyectos:** navegación a la información de un proyecto.
- **Tareas y programadores:** pantallas y llamadas a API para tareas, consulta de programadores y asignaciones.

> Las funcionalidades están representadas en el código fuente. No se ha ejecutado una prueba completa contra el backend durante esta revisión.

## Tecnologías

| Área | Tecnologías |
| --- | --- |
| **Lenguaje y plataforma** | Kotlin/JVM · Aplicación de escritorio |
| **Interfaz** | Compose Desktop · Material UI |
| **Navegación** | Voyager |
| **Comunicación HTTP** | Ktor Client (CIO) |
| **Serialización** | Kotlinx Serialization / JSON |
| **Asincronía** | Kotlin Coroutines |
| **Compilación** | Gradle Kotlin DSL |
| **Distribución configurada** | MSI (Windows), DMG (macOS), DEB (Linux) |

## Arquitectura

```text
GestorProyectosAlexsoft/
├── src/main/kotlin/
│   ├── Main.kt                 # Ventana principal y navegación inicial
│   ├── models/                 # Modelos de datos y peticiones
│   ├── network/                # Cliente HTTP e integración con la API
│   ├── screens/                # Pantallas y componentes de interfaz
│   └── utils/                  # Utilidades compartidas
├── build.gradle.kts            # Dependencias y distribución de escritorio
└── settings.gradle.kts         # Configuración del proyecto
```

La capa `network/` conecta la interfaz con los endpoints REST. Las pantallas se apoyan en los modelos de `models/` y utilizan navegación de **Voyager**.

## Capturas de la aplicación

Las siguientes capturas pertenecen a la documentación original del proyecto.

| Inicio de sesión | Bienvenida |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/3fbb1009-7d16-445a-805a-1b4fdee9175f" alt="Pantalla de inicio de sesión" width="100%"> | <img src="https://github.com/user-attachments/assets/cfa7ba2d-c3e4-4f1c-bb1a-4be268e474af" alt="Pantalla de bienvenida" width="100%"> |

| Proyectos activos | Historial de proyectos |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/cf1f0151-98f4-4001-9e24-c9bfc91c7763" alt="Vista de proyectos activos" width="100%"> | <img src="https://github.com/user-attachments/assets/b3f9138a-3455-49fe-84a5-fdced1bca48c" alt="Vista de proyectos finalizados" width="100%"> |

| Detalle de proyecto | Tareas |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/f192f943-7fa3-4a5b-8017-aabdb966e38b" alt="Detalle de proyecto" width="100%"> | <img src="https://github.com/user-attachments/assets/46ffd7aa-5662-4721-a750-8373dedd4822" alt="Gestión de tarea" width="100%"> |

## Instalación y ejecución

**Requisitos:** JDK compatible con la configuración Gradle, un entorno de desarrollo Kotlin (por ejemplo, IntelliJ IDEA) y un backend compatible iniciado localmente.

1. Clona el repositorio:
   ```bash
   git clone https://github.com/fmoorenoo/GestorProyectosAlexsoft.git
   cd GestorProyectosAlexsoft
   ```
2. Abre el proyecto en IntelliJ IDEA y sincroniza las dependencias Gradle.
3. Configura e inicia el backend que espera la aplicación. Por ejemplo, el inicio de sesión apunta a `http://127.0.0.1:5000/login`.
4. Ejecuta el punto de entrada `Main.kt` desde el IDE.

**API relacionada:** [metodos_alexsoftAPI](https://github.com/fmoorenoo/metodos_alexsoftAPI). Su configuración y disponibilidad deben comprobarse por separado.

Las opciones de empaquetado están declaradas en `build.gradle.kts`; no se ha comprobado la generación de instaladores en esta revisión.

## Estado del proyecto

**Proyecto académico / demostración técnica.** La documentación original identifica una limitación en el script de base de datos que impide asignar más de un programador por proyecto.

La autenticación de la interfaz envía un hash SHA-512 de la contraseña a un servidor HTTP local. **Esto no sustituye TLS ni un esquema de autenticación seguro para producción.** Se recomienda revisar el tratamiento de credenciales y las comunicaciones antes de cualquier despliegue real.

## Autor

**Fernando Montelongo Moreno** · Full Stack Developer · DAM + DAW

[![GitHub](https://img.shields.io/badge/GitHub-fmoorenoo-22252C?style=flat-square&logo=github)](https://github.com/fmoorenoo)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Perfil_profesional-712B39?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/fernando-montelongo/)

---

<div align="center">
  <sub>Gestor de Proyectos · Alexsoft · Proyecto de escritorio en Kotlin</sub>
</div>
