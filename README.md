<div  style="display: flex; align-items: center; justify-content: center; gap: 10px;"> <h1><img  src="https://github.com/Stock-Inc/dysnomia-front/blob/master/src/app/icon.svg" width="50" height="50" alt="Иконка"/> dysnomia backend</h1>
</div>

Dysnomia is an open-source project aimed at full transparency for users.

## 📋 Overview

- [Stack](#-stack)
- [Installation](#-installation)
- [Usage](#-usage)

## 🛠 Stack

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-007ACC?style=for-the-badge&logo=flyway&logoColor=white)


- Framework: Spring Boot 3.56
- DB: PostgreSQL
- Security: Spring Security + JWT
- Docs: Swagger/OpenAPI 3
- Deployment: Docker
- Push notifications: Firebase Cloud Messaging
- Message Broker: RabbitMQ
- Migrations: Flyway

## ⚙️ Installation

### Requirements
- Java 21+
- PostgreSQL
- Maven
- Docker

### Steps
```bash
# Clone repository
git clone https://github.com/Stock-Inc/dysnomia-backend-chat.git
cd dysnomia-backend-chat

# Build project
docker compose up --build

# All the endpoints of our project
http://localhost:8081/swagger-ui/index.html
```
## 💻 Frontend / Clients

- **Android**: [dysnomia-android](https://github.com/Stock-Inc/dysnomia-android)
- **Web**:     [dysnomia-frontend](https://github.com/Stock-Inc/dysnomia-front)
