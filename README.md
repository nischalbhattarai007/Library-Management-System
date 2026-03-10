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


---

##  Getting Started

### 1. Clone the repo
```bash
git clone https://github.com/nischalbhattarai007/Library-Management-System
cd library-management-system-grpc

./gradlew build

./gradlew run

Server will start on port 50051 by default.

Test the API

Use Evans CLI, BloomRPC, or Postman gRPC:

AddBook(BookRequest)

GetBookById(BookIdRequest)

ListBooks(Empty)

LendBook(LendRequest)

ReturnBook(ReturnRequest)

ListAuthors(Empty)

DeleteBook(DeleteBookRequest)
Add a book
{
  "title": "The Hobbit",
  "genre": "Fantasy",
  "authorName": "J.R.R. Tolkien"
}

Response : 
{
  "bookId": 1,
  "title": "The Hobbit",
  "genre": "Fantasy",
  "isLent": false,
  "lentTo": "",
  "authorName": "J.R.R. Tolkien"
}

Technologies Used

Java 17+

gRPC & Protobuf

Jakarta / Micronaut Dependency Injection

SLF4J + Lombok

Gradle

Contact

Author: Nischal Bhattaarai
GitHub: https://github.com/your-username

Email: your-email@example.com

