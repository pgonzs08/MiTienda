# 🛒 MiTienda - Sistema de Gestión e Inspección

Este proyecto es una aplicación web desarrollada con **Java** y **Spring Boot**, diseñada como un sistema de gestión para una tienda en línea. Incluye funcionalidades avanzadas de inspección y auditoría de datos en tiempo de ejecución.

---

## 📋 Tabla de Contenidos

1. [Descripción del Proyecto](#descripción-del-proyecto)
2. [Tecnologías Utilizadas](#tecnologías-utilizadas)
3. [Estructura del Proyecto](#estructura-del-proyecto)
4. [Requisitos Previos](#requisitos-previos)
5. [Instalación y Configuración](#instalación-y-configuración)
6. [Ejecución de la Aplicación](#ejecución-de-la-aplicación)
7. [Pruebas del Sistema](#pruebas-del-sistema)
8. [Despliegue y Empaquetado](#despliegue-y-empaquetado)
9. [Guión de Desarrollo y Metodología](#guión-de-desarrollo-y-metodología)

---

## 🛠️ Descripción del Proyecto

**MiTienda** es una solución desarrollada para gestionar un catálogo de productos, clientes y operaciones básicas de venta. Adicionalmente, implementa herramientas de auditoría (*UT - Unit Testing/Unit Tools*) para inspeccionar la integridad de las transacciones y verificar la persistencia de datos.

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
