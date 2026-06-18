# AI-Task-Manager

🔗 **Live demo:** https://ai-task-manager-1-w570.onrender.com (free tier — first request may take ~50s to wake)

A production-style **Java 17 + Spring Boot 3** REST API for managing tasks, secured with **JWT** authentication and backed by an in-memory **H2** database. It also exposes a simulated **AI summarization** endpoint that condenses a task description into a one-line summary without calling any external service.

The project is structured with **clean, layered architecture** (Controller → Service → Repository, with separate DTO and Security layers) and is ready to build and run inside **VS Code** with the Maven and Java extensions.

---

## Table of Contents

1. [Tech Stack](#tech-stack)
2. [Project Architecture](#project-architecture)
3. [Package Structure](#package-structure)
4. [Getting Started in VS Code](#getting-started-in-vs-code)
5. [Configuration](#configuration)
6. [API Reference](#api-reference)
7. [JWT Authentication Flow](#jwt-authentication-flow)
8. [Sample curl Commands](#sample-curl-commands)
9. [Postman Collection](#postman-collection)
10. [How AI Tools Accelerated Development](#how-ai-tools-accelerated-development)
11. [Example Prompts Used During Development](#example-prompts-used-during-development)
12. [Future Enhancements](#future-enhancements)

---

## Tech Stack

| Concern            | Technology                          |
|--------------------|-------------------------------------|
| Language           | Java 17                             |
| Framework          | Spring Boot 3.3.x                   |
| Build tool         | Maven                               |
| Web                | Spring Web (MVC, REST)              |
| Security           | Spring Security 6 + JWT (jjwt 0.12) |
| Persistence        | Spring Data JPA + Hibernate         |
| Database           | H2 (in-memory)                      |
| Password hashing   | BCrypt                              |
| Boilerplate        | Lombok                              |
| Validation         | Jakarta Bean Validation             |

---

## Project Architecture

The application follows a clean, layered architecture. Each layer has a single responsibility and depends only on the layer directly beneath it, which keeps the code testable and easy to evolve (for example, swapping the simulated AI service for a real LLM call touches only the service layer).

```
            HTTP request
                 |
                 v
 +-------------------------------+
 |        Controller layer       |  REST endpoints, request/response mapping
 |  AuthController, TaskController|
 |        AiController           |
 +---------------+---------------+
                 |
                 v
 +-------------------------------+
 |         Service layer         |  Business logic, transactions, AI heuristics
 | AuthService, TaskService,     |
 | AiSummarizationService        |
 +---------------+---------------+
                 |
                 v
 +-------------------------------+
 |       Repository layer         |  Spring Data JPA interfaces
 | UserRepository, TaskRepository |
 +---------------+---------------+
                 |
                 v
 +-------------------------------+
 |        Database (H2)           |
 +-------------------------------+

 Cross-cutting:
   DTO layer        -> request/response objects (never expose entities directly)
   Security layer   -> JwtUtil, JwtAuthenticationFilter, CustomUserDetailsService, SecurityConfig
   Exception layer  -> GlobalExceptionHandler (consistent JSON error envelope)
```

Key design decisions:

- **DTOs everywhere at the boundary.** Controllers accept and return DTOs (`TaskRequest`, `TaskResponse`, etc.) rather than JPA entities, so the persistence model can change without breaking the API contract.
- **Per-user data isolation.** Every task stores the `owner` username. Service methods always filter by the authenticated user, so one user can never read or modify another user's tasks.
- **Stateless security.** No HTTP session is created; every protected request is authenticated purely from its JWT.
- **Centralized error handling.** `GlobalExceptionHandler` converts exceptions into a consistent JSON error envelope with timestamp, status, message, and field-level validation errors.

---

## Package Structure

```
AI-Task-Manager/
├── pom.xml
├── README.md
├── sample-curl.sh
├── AI-Task-Manager.postman_collection.json
├── .gitignore
├── .vscode/
│   ├── launch.json
│   └── settings.json
└── src/
    ├── main/
    │   ├── java/com/aitaskmanager/
    │   │   ├── AiTaskManagerApplication.java
    │   │   ├── config/
    │   │   │   └── SecurityConfig.java
    │   │   ├── controller/
    │   │   │   ├── AuthController.java
    │   │   │   ├── TaskController.java
    │   │   │   └── AiController.java
    │   │   ├── service/
    │   │   │   ├── AuthService.java
    │   │   │   ├── TaskService.java
    │   │   │   └── AiSummarizationService.java
    │   │   ├── repository/
    │   │   │   ├── UserRepository.java
    │   │   │   └── TaskRepository.java
    │   │   ├── entity/
    │   │   │   ├── User.java
    │   │   │   ├── Task.java
    │   │   │   └── TaskStatus.java
    │   │   ├── dto/
    │   │   │   ├── RegisterRequest.java
    │   │   │   ├── LoginRequest.java
    │   │   │   ├── AuthResponse.java
    │   │   │   ├── TaskRequest.java
    │   │   │   ├── TaskResponse.java
    │   │   │   ├── AiSummarizeRequest.java
    │   │   │   ├── AiSummarizeResponse.java
    │   │   │   └── ApiError.java
    │   │   ├── security/
    │   │   │   ├── JwtUtil.java
    │   │   │   ├── JwtAuthenticationFilter.java
    │   │   │   └── CustomUserDetailsService.java
    │   │   └── exception/
    │   │       ├── GlobalExceptionHandler.java
    │   │       ├── ResourceNotFoundException.java
    │   │       └── BadRequestException.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/aitaskmanager/
            └── AiTaskManagerApplicationTests.java
```

---

## Getting Started in VS Code

### Prerequisites

- **JDK 17** installed and on your `PATH` (`java -version` should report 17).
- **Maven** installed (`mvn -version`). Alternatively use the Maven wrapper if you generate one.
- **VS Code** with these extensions:
  - *Extension Pack for Java* (Microsoft)
  - *Spring Boot Extension Pack* (optional but recommended)

### Steps

1. **Open the folder** in VS Code: `File → Open Folder…` and select `AI-Task-Manager`.
2. Wait for the Java language server to import the Maven project (status bar shows progress). Dependencies download automatically on first import.
3. **Run the app** in any of these ways:
   - Press **F5** (uses `.vscode/launch.json`), or
   - Open `AiTaskManagerApplication.java` and click the **Run** code-lens above `main`, or
   - From a terminal: `mvn spring-boot:run`
4. The API starts on **http://localhost:8080**.
5. Open the **H2 console** at **http://localhost:8080/h2-console**
   (JDBC URL `jdbc:h2:mem:taskdb`, user `sa`, empty password).

### Build / test from the terminal

```bash
mvn clean compile      # compile only
mvn clean test         # run tests
mvn clean package      # build runnable jar in target/
java -jar target/ai-task-manager-1.0.0.jar
```

---

## Configuration

All settings live in `src/main/resources/application.properties`:

| Property                  | Default                  | Purpose                                |
|---------------------------|--------------------------|----------------------------------------|
| `server.port`             | `8080`                   | HTTP port                              |
| `app.jwt.secret`          | Base64 demo secret       | HMAC-SHA256 signing key (>= 256 bits)  |
| `app.jwt.expiration-ms`   | `86400000` (24h)         | Token lifetime in milliseconds         |
| `spring.h2.console.enabled` | `true`                 | Enables the browser H2 console         |
| `spring.jpa.hibernate.ddl-auto` | `update`           | Auto-creates the schema on startup     |

> **Production note:** replace `app.jwt.secret` with a strong, externally supplied secret (environment variable or secrets manager) and switch to a persistent database.

---

## API Reference

Base URL: `http://localhost:8080`

### Public endpoints

| Method | Path                  | Body                                              | Description                |
|--------|-----------------------|---------------------------------------------------|----------------------------|
| POST   | `/api/auth/register`  | `{ "username", "email", "password" }`             | Create account, returns JWT|
| POST   | `/api/auth/login`     | `{ "username", "password" }`                      | Authenticate, returns JWT  |
| POST   | `/api/ai/summarize`   | `{ "description" }`                                | Simulated AI summary       |

### Protected endpoints (require `Authorization: Bearer <token>`)

| Method | Path                              | Body                                        | Description            |
|--------|-----------------------------------|---------------------------------------------|-----------------------|
| POST   | `/api/tasks`                      | `{ "title", "description", "status" }`      | Create task           |
| GET    | `/api/tasks`                      | –                                           | List your tasks       |
| GET    | `/api/tasks/{id}`                 | –                                           | Get one task          |
| PUT    | `/api/tasks/{id}`                 | `{ "title", "description", "status" }`      | Update task           |
| DELETE | `/api/tasks/{id}`                 | –                                           | Delete task           |
| GET    | `/api/tasks/search?status=TODO`   | –                                           | Filter by status      |

`status` is one of `TODO`, `IN_PROGRESS`, `DONE`.

### Example: register response

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "username": "jeevan"
}
```

### Example: AI summarize

Request:

```json
{ "description": "Complete backend microservice implementation and testing" }
```

Response:

```json
{ "summary": "Complete backend microservice implementation and testing requiring focus on complete, backend, microservice, implementation, testing." }
```

The summarizer is a deterministic Java heuristic (keyword extraction + templating) in `AiSummarizationService`. No external API is called, so it runs offline and free. The logic is isolated behind one method so it can be replaced by a real LLM call later.

---

## JWT Authentication Flow

```
 Register / Login
 ----------------
 Client                          Server
   |  POST /api/auth/login          |
   | ----------------------------> |  AuthService authenticates via
   |   {username, password}        |  AuthenticationManager + BCrypt
   |                               |  JwtUtil.generateToken(username)
   |  <---------------------------- |
   |   { token, tokenType,         |
   |     username }                |


 Accessing a protected resource
 ------------------------------
 Client                          Server
   |  GET /api/tasks                |
   |  Authorization: Bearer <jwt>  |
   | ----------------------------> |  JwtAuthenticationFilter:
   |                               |   1. read Authorization header
   |                               |   2. JwtUtil.extractUsername(token)
   |                               |   3. load UserDetails
   |                               |   4. JwtUtil.isTokenValid(...)
   |                               |   5. set SecurityContext auth
   |                               |  Controller runs, @AuthenticationPrincipal
   |                               |  gives the current username
   |  <---------------------------- |
   |   200 OK  [ ...tasks... ]      |
```

Step by step:

1. **Register or login.** Credentials are checked. Passwords are stored only as **BCrypt** hashes and never returned. On success the server signs a JWT with HMAC-SHA256 using `app.jwt.secret`. The token's subject is the username and it carries issued-at and expiry claims.
2. **Client stores the token** and sends it on every subsequent request as `Authorization: Bearer <token>`.
3. **`JwtAuthenticationFilter`** runs once per request, before Spring's `UsernamePasswordAuthenticationFilter`. It extracts and validates the token, loads the user via `CustomUserDetailsService`, and populates the `SecurityContext`.
4. **`SecurityConfig`** marks `/api/auth/**`, `/api/ai/**`, and `/h2-console/**` as public and requires authentication for everything else. The session policy is **stateless** — no server-side session is created.
5. **Invalid or missing token** ⇒ the request reaches no controller and Spring returns **401 Unauthorized** via the configured entry point.

---

## Sample curl Commands

A ready-to-run script is included as `sample-curl.sh` (it logs in, captures the token, and exercises every endpoint). Individual commands:

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"jeevan","email":"jeevan@example.com","password":"secret123"}'

# Login (copy the "token" from the response)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"jeevan","password":"secret123"}'

# Create a task (replace TOKEN)
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Build backend","description":"Complete backend microservice implementation and testing","status":"TODO"}'

# Get all tasks
curl http://localhost:8080/api/tasks -H "Authorization: Bearer TOKEN"

# Get task by id
curl http://localhost:8080/api/tasks/1 -H "Authorization: Bearer TOKEN"

# Update task
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Build backend","description":"Updated description","status":"IN_PROGRESS"}'

# Search by status
curl "http://localhost:8080/api/tasks/search?status=IN_PROGRESS" \
  -H "Authorization: Bearer TOKEN"

# Delete task
curl -X DELETE http://localhost:8080/api/tasks/1 -H "Authorization: Bearer TOKEN"

# AI summarize (public)
curl -X POST http://localhost:8080/api/ai/summarize \
  -H "Content-Type: application/json" \
  -d '{"description":"Complete backend microservice implementation and testing"}'
```

---

## Postman Collection

Import `AI-Task-Manager.postman_collection.json` into Postman. It defines `baseUrl` and `token` collection variables; the **Register** and **Login** requests automatically capture the returned JWT into `{{token}}`, so the protected requests work immediately afterwards.

---

## How AI Tools Accelerated Development

AI coding assistants were used throughout to compress what is normally a multi-day setup into a focused session. Each tool played to a different strength:

- **Claude** — used for architecture and scaffolding: laying out the clean layered package structure, drafting the JWT filter and `SecurityConfig`, writing the simulated `AiSummarizationService`, and producing this documentation. Its large context window made it well suited to generating many interrelated files consistently and explaining the JWT flow end to end.
- **Cursor** — used as the in-editor pair programmer for multi-file edits: renaming across the project, wiring DTOs to services, and applying repository query method changes with whole-repo awareness.
- **GitHub Copilot** — used for line-level autocompletion while typing: boilerplate getters/builders, repository method signatures (`findByOwnerAndStatus`), and repetitive curl/Postman entries.
- **ChatGPT** — used as a quick reference and rubber-duck: clarifying Spring Security 6 lambda DSL changes, the `jjwt` 0.12 API, and Jakarta (vs. javax) namespace migration in Spring Boot 3.

The net effect was faster boilerplate, fewer trips to documentation, and more time spent on the design decisions that actually matter (data isolation, stateless security, swappable AI layer). Every AI-generated snippet was reviewed, compiled, and tested before being kept.

## Example Prompts Used During Development

These are representative prompts that shaped the codebase:

- *"Generate a Spring Boot 3 `SecurityConfig` using the new lambda DSL: stateless sessions, permit `/api/auth/**`, require JWT for everything else, register a custom `OncePerRequestFilter`, and BCrypt encoder."*
- *"Write a `JwtUtil` using io.jsonwebtoken 0.12 with `generateToken`, `extractUsername`, and `isTokenValid`, signing with an HS256 key built from a Base64 secret."*
- *"Create a `JwtAuthenticationFilter` that reads the Bearer token, validates it, loads `UserDetails`, and sets the `SecurityContext`; fail gracefully on invalid tokens."*
- *"Design a clean layered task CRUD: entity, DTOs, repository with `findByOwnerAndStatus`, service that enforces per-user ownership, and a REST controller using `@AuthenticationPrincipal`."*
- *"Implement a simulated AI summarizer in pure Java — extract keywords, drop stop words, and build a one-sentence summary — no external API calls."*
- *"Write a `@RestControllerAdvice` global exception handler returning a consistent JSON error envelope with field-level validation messages."*
- *"Produce a Postman collection that auto-captures the JWT from login into a collection variable."*

## Future Enhancements

- **Real LLM integration (OpenAI / Anthropic).** Replace the heuristic in `AiSummarizationService` with a call to the OpenAI or Anthropic API behind the same method signature. Add an `AiProvider` interface with `HeuristicProvider` and `OpenAiProvider` implementations selected by a config property, plus a `WebClient`/`RestClient`, API-key configuration, retries, and timeouts.
- **Microservice decomposition.** Split into independently deployable services — an **Auth service** (issues JWTs), a **Task service** (CRUD), and an **AI service** (summarization) — coordinated by an **API gateway** (Spring Cloud Gateway) with service discovery (Eureka/Consul) and a shared JWT trust boundary. Move from in-memory H2 to a per-service database.
- **Refresh tokens & roles.** Add short-lived access tokens with refresh tokens, token revocation/blacklist, and role-based authorization (`ROLE_USER`, `ROLE_ADMIN`).
- **Persistent database & migrations.** Swap H2 for PostgreSQL and manage schema with Flyway or Liquibase.
- **Observability & docs.** Add springdoc-openapi (Swagger UI), structured logging, Micrometer metrics, and health checks via Spring Boot Actuator.
- **Containerization & CI/CD.** Provide a `Dockerfile` and `docker-compose`, and a GitHub Actions pipeline that builds, tests, and publishes images.
- **Testing depth.** Add unit tests for services and `@WebMvcTest` slice tests for controllers with mocked security.
