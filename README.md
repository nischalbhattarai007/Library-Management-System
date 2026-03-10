# Library Management gRPC Service

[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/)  
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)  
[![Build](https://img.shields.io/badge/Build-Gradle-yellow.svg)](https://gradle.org/)

A **Library Management System** using **gRPC** and **Java**, with clean architecture including endpoints, services, repositories, DTOs, and mappers. Manage books and authors efficiently with lending/return features.

---

##  Features

- Add, list, lend, return, and delete books
- List authors
- gRPC endpoints with validation
- DTOs and Mapper classes for clean conversion
- Logging via SLF4J/Lombok
- Custom exceptions for better error handling

---
````
##  Project Architecture
library-management
│
├── endpoint # gRPC endpoints
├── service # Business logic
├── repository # Database access
├── entity # DB entities
├── dto # Data Transfer Objects
├── mapper # DTO ↔ gRPC conversion
├── exception # Custom exceptions
└── proto # gRPC Protobuf definitions
````

---
