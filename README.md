# 🛒 MiTienda - Sistema de Gestión e Inspección

Este proyecto es una aplicación web desarrollada con **Java** y **Spring Boot**, diseñada como un sistema de gestión para una tienda en línea. Incluye funcionalidades avanzadas de inspección y auditoría de datos en tiempo de ejecución.

---

## 📋 Tabla de Contenidos

1. [Descripción del Proyecto](#descripción-del-proyecto)
2. [¿Cómo ejecuto MiTienda?](#¿cómo-ejecuto-mitienda?)
3. [Tecnologías Utilizadas](#tecnologías-utilizadas)
4. [Estructura del Proyecto](#estructura-del-proyecto)
5. [Requisitos Previos](#requisitos-previos)
6. [Instalación y Configuración](#instalación-y-configuración)
7. [Ejecución de la Aplicación](#ejecución-de-la-aplicación)
8. [Pruebas del Sistema](#pruebas-del-sistema)
9. [Despliegue y Empaquetado](#despliegue-y-empaquetado)
10. [Guión de Desarrollo y Metodología](#guión-de-desarrollo-y-metodología)

---

## 🛠️ Descripción del Proyecto

**MiTienda** es una solución desarrollada para gestionar un catálogo de productos, clientes y operaciones básicas de venta. Adicionalmente, implementa herramientas de auditoría (*UT - Unit Testing/Unit Tools*) para inspeccionar la integridad de las transacciones y verificar la persistencia de datos.

---

## ¿Cómo ejecuto MiTienda?

Para ejecutar la aplicación es necesario comprender primero cómo interactúa su arquitectura técnica.

### ⚙️ Funcionamiento del Stack Tecnológico

El proyecto está construido sobre un stack moderno en **Java 17** y **Spring Boot**:

- **Servidor Web Embebido:** Spring Boot incorpora un servidor **Apache Tomcat** integrado. No es necesario instalar ni configurar un servidor externo; la aplicación se empaqueta junto con su runtime y se despliega automáticamente en el puerto `8080`.
- **Capa de Persistencia e Inicialización:** Utiliza **Spring Data JPA** con una base de datos **H2 en memoria**. Al arrancar, Hibernate genera automáticamente la estructura de tablas y ejecuta las tareas de inicialización.
- **Seguridad e Inicialización del Administrador:** Mediante la clase de configuración `SecurityConfig`, la aplicación ejecuta un componente `CommandLineRunner` en la fase de arranque que verifica si existe el usuario administrador en la base de datos. Si no existe, lo crea automáticamente con los siguientes credenciales por defecto:

| Usuario | Contraseña | Rol asignado |
| :--- | :--- | :--- |
| `admin` | `123&qwe&asD` | `ADMIN` |

> ⚠️ **¡ADVERTENCIA DE SEGURIDAD PARA ENTORNO DE PRODUCCIÓN!**
> Si vas a desplegar esta aplicación en un entorno público o accesible desde internet, **debes modificar de inmediato las credenciales por defecto** directamente en el archivo:
> `src/main/java/es/ugr.dss/MiTienda/config/SecurityConfig.java`

---

### 🚀 Métodos de Ejecución

Puedes arrancar **MiTienda** utilizando cualquiera de las siguientes tres alternativas según tu entorno de desarrollo:

#### Opción 1: Desde la Terminal (Mediante Maven Wrapper)

Es el método estándar e independiente de IDE. Utiliza el ejecutable `mvnw` incluido en la raíz del proyecto (no requiere tener Maven instalado globalmente en el sistema).

1. Abre una terminal en la carpeta raíz del proyecto.
2. Ejecuta el comando según tu sistema operativo:

   - **En Linux / macOS:**
     ```bash
     ./mvnw spring-boot:run
     ```
   - **En Windows (PowerShell / CMD):**
     ```cmd
     .\mvnw.cmd spring-boot:run
     ```

3. Una vez veas en consola el mensaje `Started MiTiendaApplication in X seconds`, accede en tu navegador a:
   👉 **`http://localhost:8080`** (y a la consola H2 en **`http://localhost:8080/h2-console`**).

---

#### Opción 2: Usando VS Code (con Spring Tools Suite / Java Extension Pack)

1. Abre la carpeta del proyecto `MiTienda` en **Visual Studio Code**.
2. Asegúrate de tener instalada la extensión **Spring Boot Extension Pack** (o la extensión oficial de *Spring Tools*).
3. Despliega el panel de **Spring Boot Dashboard** en la barra lateral izquierda.
4. En la sección **Apps**, localiza `MiTienda` o `MiTiendaApplication` y haz clic en el botón de reproducción **▶️ (Start)** o pulsa `F5`.
5. Alternativamente, abre la clase principal `src/main/java/es/ugr/dss/MiTienda/MiTiendaApplication.java` y pulsa sobre la opción **Run** que aparece sobre el método `main()`.

---

#### Opción 3: Usando Eclipse / Spring Tool Suite (STS)

1. Abre **Spring Tool Suite (Eclipse)** e importa el proyecto:
   - Ve a `File` ➔ `Import...` ➔ `Maven` ➔ `Existing Maven Projects`.
   - Selecciona la carpeta raíz de `MiTienda` y haz clic en **Finish**.
2. En la vista **Boot Dashboard** (esquina inferior izquierda):
   - Selecciona la aplicación `MiTienda`.
   - Haz clic en el icono verde de arranque **(Run / Debug)**.
3. *Alternativa clásica:* Haz clic derecho sobre el proyecto o sobre el archivo `MiTiendaApplication.java` ➔ `Run As` ➔ **Spring Boot App**.

---

## 🚀 Tecnologías Utilizadas

* **Lenguaje:** Java v17
* **Framework Principal:** Spring Boot 3.5.x
* **Gestor de Dependencias:** Apache Maven
* **Capa de Persistencia:** Spring Data JPA / Hibernate
* **Base de Datos:** H2 (En memoria para desarrollo/pruebas) / MySQL
* **Testing:** JUnit 5, AssertJ, Mockito
* **Herramientas de Construcción:** Maven Wrapper (`mvnw` / `mvnw.cmd`)

---

## 📁 Estructura del Proyecto

```text
MiTienda/
├── .github/              # Flujos de trabajo y CI/CD (GitHub Actions)
├── src/
│   ├── main/
│   │   ├── java/         # Código fuente de la aplicación (Controladores, Servicios, Modelos, Repositorios)
│   │   └── resources/    # Archivos de configuración (application.properties / .yml, plantillas, etc.)
│   └── test/
│       └── java/         # Pruebas unitarias e integración
├── target/               # Archivos compilados y artefato JAR
├── mvnw / mvnw.cmd       # Wrapper de Maven para ejecutar sin instalación global
├── pom.xml               # Configuración de dependencias y plugins de Maven
└── README.md             # Documentación del proyecto
