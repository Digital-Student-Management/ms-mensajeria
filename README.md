# 📨 Microservicio de Mensajería Integrada (ms-mensajeria)

Este repositorio contiene el código fuente del **Microservicio de Sistema de Mensajería Integrada**, componente clave de la arquitectura distribuida del Sistema Integral de Gestión Estudiantil Digital para el **Colegio Bernardo O'Higgins de Coquimbo**.

Este módulo actúa como una plataforma de comunicación bidireccional y segura, permitiendo el intercambio de información institucional, alertas, notificaciones y reportes entre los distintos actores de la comunidad educativa (Estudiantes, Apoderados, Docentes y Directivos).

---

## 🚀 Descripción de la Arquitectura y Dominio

El `ms-mensajeria` está diseñado bajo los principios de **Bajo Acoplamiento** y **Desacoplamiento de Datos**. Administra su propia persistencia de datos de forma independiente y se conecta lógicamente con el resto del ecosistema a través de **Referencias Suaves (Soft References)** empleando los identificadores únicos (`id`) de los usuarios provenientes del microservicio central (`ms-usuarios`).

### 📦 Entidad Principal y Modelo de Datos
El núcleo de este microservicio gestiona la persistencia de los hilos comunicativos a través de una estructura relacional limpia:

* **Entidad `Mensaje` (`mensaje`):** Representa un mensaje individual dentro del sistema. Registra el cuerpo del texto, las marcas temporales automáticas, el hilo temático de la conversación, el estado de lectura y los identificadores numéricos abstractos del emisor (`idRemitente`) y del receptor (`idDestinatario`).

---

## 🛠️ Stack Tecnológico

* **Lenguaje de Programación:** Java 21 (LTS)
* **Framework Core:** Spring Boot 4.0.6
* **Gestor de Dependencias:** Maven
* **Base de Datos Relacional:** MySQL 8.x
* **Mapeo Objeto-Relacional (ORM):** Spring Data JPA / Hibernate 7.x
* **Documentación Interactiva:** SpringDoc OpenAPI 3.0.2 (Swagger UI)
* **Utilidades:** Lombok (Getters, Setters y DTO mappers simplificados)
* **Validación de Datos:** Jakarta Validation

---

## 📁 Estructura del Proyecto

La organización del código sigue el patrón limpio segmentado por capas de responsabilidad:

```text
ms-mensajeria/
├── peticiones.rest                          ← Archivo de pruebas rápidas (REST Client)
├── pom.xml                                  ← Definición de dependencias Maven
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── ms_mensajeria/
        │               ├── MsMensajeriaApplication.java   ← Clase principal de arranque
        │               ├── config/                       ← Configuraciones de red y seguridad
        │               │   ├── RestTemplateConfig.java   ← Consumo de APIs externas
        │               │   └── WebConfig.java            ← Habilitación de CORS globales
        │               ├── controllers/                  ← Controladores REST expuestos
        │               │   └── MensajeController.java
        │               ├── dtos/                         ← Clases de transferencia de datos (DTOs)
        │               │   ├── MensajeRequestDTO.java
        │               │   ├── MensajeResponseDTO.java
        │               │   └── MensajeUpdateDTO.java
        │               ├── models/                       ← Entidad de persistencia JPA
        │               │   └── Mensaje.java
        │               ├── repositories/                 ← Abstracción de consultas a la BD
        │               │   └── MensajeRepository.java
        │               └── services/                     ← Capa de lógica de negocio
        │                   ├── MensajeService.java
        │                   └── FileStorageService.java   ← Soporte para adjuntos futuros
        └── resources/
            └── application.properties                ← Configuración de puerto y base de datos
```

---

## ⚙️ Instalación y Despliegue Local

### 1. Clonar y Configurar Entorno
Asegúrate de contar con el puerto **8083** libre en tu máquina local.

```bash
git clone [https://github.com/TuOrganizacion/ms-mensajeria.git](https://github.com/TuOrganizacion/ms-mensajeria.git)
cd ms-mensajeria
```

### 2. Variables de Lanzamiento (`.vscode/launch.json`)
El microservicio emplea variables de entorno locales para resguardar las credenciales del motor de base de datos. Configura tu entorno en VS Code con el siguiente molde:

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Arrancar MS Mensajería",
            "request": "launch",
            "mainClass": "com.example.ms_mensajeria.MsMensajeriaApplication",
            "projectName": "ms-mensajeria",
            "env": {
                "DB_URL": "jdbc:mysql://localhost:3306/ms_mensajeria_db?createDatabaseIfNotExist=true&serverTimezone=UTC",
                "DB_USER": "root",
                "DB_PASSWORD": "tu_contraseña_local"
            }
        }
    ]
}
```

### 3. Ejecutar la Aplicación
Inicia el sistema desde la interfaz gráfica de VS Code o a través de la consola de comandos usando el Maven Wrapper:

```bash
./mvnw spring-boot:run
```

---

## 🔌 Documentación de la API (Swagger UI)

Al levantar el entorno de desarrollo, puedes navegar al catálogo interactivo de Swagger para inspeccionar la firma técnica de los métodos HTTP, códigos de respuesta y esquemas JSON:

`http://localhost:8083/swagger-ui.html`

### Resumen de Endpoints Disponibles (`/api/mensajes`)

| Método HTTP | Ruta Completa | Descripción Funcional |
|---|---|---|
| `POST` | `/api/mensajes` | Registra y despacha un nuevo mensaje en el sistema. |
| `GET` | `/api/mensajes` | Lista la totalidad de mensajes guardados en el historial. |
| `GET` | `/api/mensajes/{id}` | Recupera la información específica de un mensaje por su ID. |
| `PUT` | `/api/mensajes/{id}` | Actualiza el estado de un mensaje (Ej: marcar como LEÍDO). |
| `DELETE` | `/api/mensajes/{id}` | Realiza la eliminación física de un mensaje específico. |

---

## 🧪 Pruebas con la Extensión REST Client

El repositorio incluye el archivo `peticiones.rest` en su raíz para agilizar el testeo de los controladores directamente desde el editor de código:

1. Instala la extensión **REST Client** desde el Marketplace de VS Code.
2. Abre el archivo `peticiones.rest`.
3. Haz clic en el botón flotante **`Send Request`** ubicado sobre cada bloque HTTP para interactuar con la base de datos en tiempo real.