# 🧪 spring-user-service
> **Spring Boot 2.7.x sample project — intentionally uses old patterns for SpringShift migration testing**

A fully functional User Management REST API built with Spring Boot 2.7.18 and Java 11.
This project is designed as a **migration test target** for [SpringShift](../SpringShift-MigrationTool.html).

---

## ⚠️ Known Migration Issues (intentional)

This project deliberately uses **deprecated / removed APIs** that SpringShift will fix:

| File | Old Pattern | Target Fix |
|---|---|---|
| `pom.xml` | Spring Boot `2.7.18`, Java `11` | → `3.5.0`, Java `21` |
| `User.java` | `javax.persistence.*` | → `jakarta.persistence.*` |
| `AuditableEntity.java` | `javax.persistence.*` | → `jakarta.persistence.*` |
| `CreateUserRequest.java` | `javax.validation.*` | → `jakarta.validation.*` |
| `UpdateUserRequest.java` | `javax.validation.*` | → `jakarta.validation.*` |
| `UserService.java` | `javax.transaction.*` | → `jakarta.transaction.*` |
| `CustomUserDetailsService.java` | `javax.transaction.*` | → `jakarta.transaction.*` |
| `SecurityConfig.java` | `extends WebSecurityConfigurerAdapter` | → `SecurityFilterChain` bean |
| `SecurityConfig.java` | `.antMatchers()` | → `.requestMatchers()` |
| `SecurityConfig.java` | `.authorizeRequests()` | → `.authorizeHttpRequests()` |
| `SecurityConfig.java` | `@EnableGlobalMethodSecurity` | → `@EnableMethodSecurity` |
| `GlobalExceptionHandler.java` | `javax.validation.*` | → `jakarta.validation.*` |
| `UserControllerIntegrationTest.java` | `org.springframework.boot.web.server.LocalServerPort` | → `org.springframework.boot.test.web.server.LocalServerPort` |
| `application.properties` | `server.max-http-header-size` | → `server.max-http-request-header-size` |
| `pom.xml` | `springdoc-openapi-ui 1.7.0` | → `springdoc-openapi-starter-webmvc-ui 2.x` |
| `pom.xml` | `jackson-databind 2.13.0` (CVE) | → `2.18.2` |
| `pom.xml` | `snakeyaml 1.30` (CVE) | → `2.3` |
| `pom.xml` | `commons-text 1.9` (CVE-2022-42889) | → `1.13.0` |

---

## 🏗️ Project Structure

```
src/
├── main/java/com/example/userservice/
│   ├── UserServiceApplication.java
│   ├── controller/
│   │   └── UserController.java          ← REST endpoints
│   ├── service/
│   │   └── UserService.java             ← Business logic (javax.transaction)
│   ├── repository/
│   │   └── UserRepository.java          ← Spring Data JPA
│   ├── entity/
│   │   ├── User.java                    ← JPA entity (javax.persistence)
│   │   └── AuditableEntity.java         ← Base entity (javax.persistence)
│   ├── dto/
│   │   ├── CreateUserRequest.java       ← (javax.validation)
│   │   ├── UpdateUserRequest.java       ← (javax.validation)
│   │   ├── UserResponse.java
│   │   └── ApiResponse.java
│   ├── security/
│   │   ├── SecurityConfig.java          ← OLD WebSecurityConfigurerAdapter
│   │   └── CustomUserDetailsService.java
│   ├── config/
│   │   ├── SwaggerConfig.java           ← Springdoc 1.x
│   │   └── DataInitializer.java
│   └── exception/
│       ├── GlobalExceptionHandler.java  ← (javax.validation)
│       ├── ResourceNotFoundException.java
│       └── DuplicateResourceException.java
└── test/java/com/example/userservice/
    ├── UserServiceTest.java
    └── UserControllerIntegrationTest.java
```

---

## 🚀 Running Locally (Spring Boot 2.7.x)

**Prerequisites:** Java 11+, Maven 3.6+

```bash
# Clone and run
git clone https://github.com/YOUR_USERNAME/spring-user-service.git
cd spring-user-service
mvn spring-boot:run
```

| Endpoint | URL |
|---|---|
| API Base | http://localhost:8080/api/v1/users |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| H2 Console | http://localhost:8080/h2-console |
| Actuator Health | http://localhost:8080/actuator/health |

**Default credentials (seeded on startup):**
| Username | Password | Role |
|---|---|---|
| `admin` | `Admin@1234` | ADMIN |
| `john.doe` | `Password@123` | USER |
| `jane.smith` | `Password@123` | MODERATOR |

---

## 🔄 Migrate with SpringShift

1. Open **SpringShift-MigrationTool.html** in your browser
2. Paste this repo URL: `https://github.com/YOUR_USERNAME/spring-user-service`
3. Click **Fetch Repo** → select all files → **Start Migration**
4. SpringShift will automatically fix all the issues listed above

---

## 📋 REST API Endpoints

| Method | Endpoint | Role | Description |
|---|---|---|---|
| POST | `/api/v1/users` | ADMIN | Create user |
| GET | `/api/v1/users` | ADMIN, MODERATOR | List all users (paged) |
| GET | `/api/v1/users/{id}` | ADMIN, MODERATOR | Get by ID |
| GET | `/api/v1/users/username/{username}` | ADMIN, MODERATOR | Get by username |
| GET | `/api/v1/users/search?keyword=` | ADMIN, MODERATOR | Search users |
| GET | `/api/v1/users/role/{role}` | ADMIN | Filter by role |
| PUT | `/api/v1/users/{id}` | ADMIN | Update user |
| DELETE | `/api/v1/users/{id}` | ADMIN | Delete user |
| PATCH | `/api/v1/users/{id}/toggle-status` | ADMIN | Enable/disable |
| GET | `/api/v1/users/stats` | ADMIN | User statistics |
