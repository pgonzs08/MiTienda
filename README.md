# 🛒 MiTienda

Aplicación web de tienda en línea desarrollada con **Java 17** y **Spring Boot 3.5**. Permite consultar y filtrar un catálogo de productos, añadirlos a un carrito, registrarse e iniciar sesión, y gestionar el catálogo desde un panel de administración.

![Java 17](https://img.shields.io/badge/Java-17-orange)
![Spring Boot 3.5](https://img.shields.io/badge/Spring%20Boot-3.5-6db33f)
![Maven](https://img.shields.io/badge/build-Maven-c71a36)
![Licencia MIT](https://img.shields.io/badge/licencia-MIT-blue)

---

## Tabla de contenidos

1. [Funcionalidades](#funcionalidades)
2. [Tecnologías](#tecnologías)
3. [Requisitos previos](#requisitos-previos)
4. [Ejecución](#ejecución)
5. [Usuario administrador](#usuario-administrador)
6. [Rutas de la aplicación](#rutas-de-la-aplicación)
7. [API REST](#api-rest)
8. [Estructura del proyecto](#estructura-del-proyecto)
9. [Base de datos](#base-de-datos)
10. [Pruebas](#pruebas)
11. [Empaquetado](#empaquetado)
12. [Integración continua](#integración-continua)
13. [Seguridad: puntos pendientes](#seguridad-puntos-pendientes)
14. [Metodología de desarrollo](#metodología-de-desarrollo)
15. [Licencia](#licencia)

---

## Funcionalidades

- **Catálogo público:** búsqueda por nombre y filtro por precio mínimo y máximo.
- **Carrito:** cada sesión mantiene su propio carrito con la cantidad de cada producto. Al añadir un producto aparece un aviso con la opción de deshacer.
- **Cuentas de usuario:** registro de nuevos usuarios (rol `USER`) e inicio de sesión con formulario. Las contraseñas se guardan cifradas con BCrypt.
- **Panel de administración:** alta, edición y borrado de productos.
- **Exportación:** descarga de los productos como script SQL (`products.sql`).
- **API REST** de productos y usuarios bajo `/api`.
- **Páginas de error** personalizadas (403, 404, 405, 5xx y genérica).

---

## Tecnologías

| Capa | Tecnología |
| :--- | :--- |
| Lenguaje | Java 17 |
| Framework | Spring Boot 3.5.16 (Spring MVC, Tomcat embebido) |
| Persistencia | Spring Data JPA, Hibernate |
| API REST | Spring Data REST |
| Seguridad | Spring Security (inicio de sesión con formulario, roles, BCrypt) |
| Vistas | Thymeleaf y `thymeleaf-extras-springsecurity6` |
| Estilos e iconos | Bootstrap 5.3.0 y Bootstrap Icons 1.11.0 (por CDN) |
| JavaScript | Script propio sin librerías (`products.js`) |
| Base de datos | H2 en fichero |
| Construcción | Maven y Maven Wrapper |
| Pruebas | JUnit 5, Mockito, AssertJ, MockMvc y Spring Security Test |
| CI | GitHub Actions |

El proyecto declara también **Lombok**, aunque el código actual no lo utiliza.

> Las versiones de las dependencias las gestiona `spring-boot-starter-parent`, por lo que no aparecen una a una en el `pom.xml`.

---

## Requisitos previos

- **JDK 17** o superior.
- Conexión a internet la primera vez (Maven descarga las dependencias) y para cargar Bootstrap desde el CDN.
- **No** hace falta instalar Maven ni un servidor de aplicaciones: el proyecto incluye el Maven Wrapper y Tomcat va embebido.

---

## Ejecución

### Desde la terminal (recomendado)

En la carpeta raíz del proyecto:

```bash
# Linux / macOS
./mvnw spring-boot:run
```

```cmd
:: Windows
.\mvnw.cmd spring-boot:run
```

Cuando aparezca `Started MiTiendaApplication`, abre <http://localhost:8080>.

### Desde un IDE

- **VS Code:** instala *Spring Boot Extension Pack*, abre el panel *Spring Boot Dashboard* y pulsa **Start** sobre `MiTienda`.
- **Eclipse / Spring Tool Suite:** `File > Import > Maven > Existing Maven Projects`, selecciona la carpeta del proyecto y ejecútalo con *Run As > Spring Boot App*.

En ambos casos la clase de arranque es `es.ugr.dss.MiTienda.MiTiendaApplication`.

---

## Usuario administrador

Al arrancar, `SecurityConfig` comprueba si existe el usuario `admin` y, si no existe, lo crea con estas credenciales:

| Usuario | Contraseña | Rol |
| :--- | :--- | :--- |
| `admin` | `123&qwe&asD` | `ADMIN` |

> ⚠️ **Antes de publicar la aplicación, cambia estas credenciales.** Están escritas en `src/main/java/es/ugr/dss/MiTienda/config/SecurityConfig.java`.

---

## Rutas de la aplicación

### Páginas web

| Ruta | Método | Acceso | Descripción |
| :--- | :--- | :--- | :--- |
| `/`, `/index` | GET | Público | Página de inicio |
| `/products` | GET | Público | Catálogo. Admite `query`, `minPrice` y `maxPrice` |
| `/register` | GET, POST | Público | Registro de usuarios |
| `/login` | GET, POST | Público | Inicio de sesión |
| `/logout` | POST | Sesión iniciada | Cierre de sesión |
| `/cart` | GET | Sesión iniciada | Ver el carrito |
| `/cart/add/{id}` | POST | Sesión iniciada | Añadir un producto |
| `/cart/undo/{id}`, `/cart/remove/{id}` | POST | Sesión iniciada | Quitar un producto |
| `/admin` | GET | `ADMIN` | Panel de administración |
| `/admin/export` | GET | `ADMIN` | Descarga `products.sql` |
| `/products/add` | GET, POST | `ADMIN` | Alta de producto |
| `/products/edit/{id}` | GET | `ADMIN` | Formulario de edición |
| `/products/update/{id}` | POST | Sesión iniciada* | Guardar la edición |
| `/products/delete/{id}` | POST | `ADMIN` | Borrar un producto |

\* Ver [Seguridad: puntos pendientes](#seguridad-puntos-pendientes).

### Consola de H2

Disponible en <http://localhost:8080/h2-console> y accesible solo con la sesión iniciada. Usa la URL JDBC `jdbc:h2:file:./data/shopdb.h2` y el usuario y la contraseña definidos en `application.properties`.

---

## API REST

Spring Data REST publica los repositorios bajo el prefijo `/api`.

| Ruta | Acceso | Descripción |
| :--- | :--- | :--- |
| `/api/products/**` | Público, todos los métodos | Consulta, alta, edición y borrado de productos |
| `/api/users/**` | `ADMIN` | Gestión de usuarios |

Los productos se serializan con estos nombres de campo:

```json
{
  "productoId": 1,
  "productoNombre": "Teclado mecánico",
  "price": 49.9
}
```

---

## Estructura del proyecto

```text
MiTienda/
├── .github/workflows/maven.yml     # Integración continua
├── data/                           # Base de datos H2 (se crea al arrancar)
├── src/
│   ├── main/
│   │   ├── java/es/ugr/dss/MiTienda/
│   │   │   ├── config/             # SecurityConfig
│   │   │   ├── controller/         # Product, Cart, Auth, Registration, View, Export
│   │   │   ├── service/            # Product, Cart, ExportDatabase, CustomUserDetails
│   │   │   ├── repository/         # ProductRepo, UserRepo
│   │   │   ├── model/              # Product, User
│   │   │   ├── MiTiendaApplication.java
│   │   │   └── ServletInitializer.java
│   │   └── resources/
│   │       ├── templates/          # Páginas Thymeleaf (y error/)
│   │       ├── static/js/          # products.js
│   │       └── application.properties
│   └── test/
│       ├── java/es/ugr/dss/MiTienda/   # Pruebas unitarias y de integración
│       └── resources/application-test.properties
├── mvnw / mvnw.cmd                 # Maven Wrapper
├── pom.xml
├── LICENSE
└── README.md
```

La aplicación sigue una arquitectura por capas: **controlador → servicio → repositorio → modelo**. El carrito (`CartService`) es un componente de ámbito de sesión que guarda, para cada producto, la cantidad añadida.

---

## Base de datos

- Se usa **H2 almacenada en fichero** (`./data/shopdb.h2`), por lo que los datos **se conservan entre ejecuciones**. La carpeta `data/` se crea sola en el directorio desde el que arrancas la aplicación.
- Hibernate crea y actualiza las tablas automáticamente (`spring.jpa.hibernate.ddl-auto=update`).
- Entidades:
  - `Product`: `id` (generado), `name` y `price`.
  - `User` (tabla `app_users`): `username` (clave), `password` cifrada y `role`.
- Para empezar con una base de datos vacía, detén la aplicación y borra la carpeta `data/`.

---

## Pruebas

```bash
./mvnw test
```

Hay 14 clases de test y 43 casos que cubren controladores, servicios, modelos y seguridad. Usan JUnit 5, Mockito y MockMvc, y se ejecutan con el perfil `test` (`application-test.properties`), que utiliza una base de datos H2 en memoria que se crea y se destruye en cada ejecución.

---

## Empaquetado

```bash
./mvnw clean package
java -jar target/MiTienda-0.0.1-SNAPSHOT.jar
```

El resultado es un JAR ejecutable con Tomcat embebido. El proyecto incluye también `ServletInitializer`, por si se necesita desplegarlo en un servidor de aplicaciones externo.

---

## Integración continua

El flujo `.github/workflows/maven.yml` (*Java CI with Maven*) se ejecuta en cada `push` y en cada *pull request* hacia `main`. Prepara JDK 17 (Temurin), compila el proyecto, lanza las pruebas con `mvn -B package` y envía el grafo de dependencias a GitHub.

---

## Seguridad: puntos pendientes

Antes de usar la aplicación fuera de un entorno de desarrollo, conviene resolver estos puntos:

- **Credenciales en el código.** La contraseña del administrador está en `SecurityConfig.java` y las de la base de datos en `application.properties`. Se recomienda moverlas a variables de entorno.
- **API de productos sin autenticación.** `/api/products/**` acepta todos los métodos sin iniciar sesión y el CSRF está desactivado para `/api/**`. Cualquiera puede crear, modificar o borrar productos.
- **Edición sin rol de administrador.** `POST /products/update/{id}` solo exige haber iniciado sesión, no tener el rol `ADMIN`.

---

## Metodología de desarrollo

El proyecto sigue **GitHub Flow** con integración continua: la rama `main` siempre debe estar estable.

### 1. Issues

Toda funcionalidad nueva, corrección o refactorización empieza con una **Issue**.

1. Abre la Issue con un título claro (por ejemplo, `[Feature] Añadir filtro de precio`) y describe el problema, el comportamiento esperado y los criterios de aceptación.
2. Elige una Issue **sin persona asignada** y asígnatela antes de empezar, para no duplicar trabajo.

### 2. Ramas

**No se hacen commits directamente sobre `main`.** Crea una rama a partir de ella:

```bash
git checkout main
git pull origin main
git checkout -b feature/issue-12-filtros-catalogo
```

Haz commits pequeños y con mensajes descriptivos:

```bash
git commit -m "feat: implementar filtro de precio min/max en ProductRepo (#12)"
```

### 3. Pull Requests

1. Sube la rama con `git push origin feature/issue-12-filtros-catalogo` y abre una *Pull Request* hacia `main`.
2. Enlaza la Issue en la descripción con `Closes #12` o `Fixes #12`.
3. Otra persona del equipo debe revisar el código y las pruebas automáticas deben pasar.
4. Tras la aprobación, se hace el *merge* a `main` y se elimina la rama.

```text
[ Issue sin asignar ] ➔ [ Asignarse ] ➔ [ Rama ] ➔ [ Commits ] ➔ [ Pull Request ] ➔ [ Revisión y CI ] ➔ [ Merge a main ]
```

---

## Licencia

Proyecto distribuido bajo la licencia **MIT**. Consulta el archivo [LICENSE](LICENSE).

Autor: Pablo González Santamarta · Repositorio: <https://github.com/pgonzs08/MiTienda>