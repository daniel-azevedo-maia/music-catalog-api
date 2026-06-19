# Music Catalog API

A professional Spring Boot REST API for managing a music catalog.

This project is being developed as a practical, production-oriented backend application using **Java 21**, **Spring Boot**, **PostgreSQL**, **Flyway**, **JPA/Hibernate**, **Docker Compose**, **Testcontainers**, automated tests, and a clean layered architecture.

The main goal is to build a realistic backend system step by step, applying professional practices such as versioned database migrations, isolated testing, feature branches, small commits, and explicit validation of each development checkpoint.

---

## Project Status

This project is currently under active development.

Implemented so far:

- Initial Spring Boot project setup
- Health check endpoint
- Professional application configuration using profiles
- PostgreSQL environment with Docker Compose
- Flyway database migration setup
- JPA/Hibernate configuration
- `Artist` domain entity
- `ArtistRepository`
- Repository integration tests with Testcontainers

Planned next steps:

- Artist service layer
- Artist REST controller
- DTO validation
- Global exception handling
- API documentation with OpenAPI/Swagger
- Additional music catalog domains such as albums, tracks, genres, and users
- Authentication and authorization
- CI pipeline with GitHub Actions

---

## Domain Overview

The application is a music catalog backend.

The system is intended to manage entities such as:

- Artists
- Albums
- Tracks
- Genres
- Catalog metadata

The first real domain implemented is `Artist`.

Current `Artist` attributes include:

- `id`
- `name`
- `biography`
- `country`
- `active`
- `createdAt`
- `updatedAt`

The project uses a soft-deactivation approach for artists, meaning an artist can be marked as inactive instead of being physically removed from the database.

---

## Technology Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.15 |
| Build Tool | Maven |
| Web Layer | Spring Web |
| Persistence | Spring Data JPA |
| ORM | Hibernate |
| Database | PostgreSQL 17 |
| Migrations | Flyway |
| Validation | Jakarta Bean Validation |
| Boilerplate Reduction | Lombok |
| Local Infrastructure | Docker Compose |
| Integration Testing | Testcontainers |
| Test Framework | JUnit 5 |
| Assertions | AssertJ |
| API Testing | MockMvc |
| Version Control | Git |

---

## Architecture

The project follows a **clean layered architecture**.

The current structure is organized by domain/feature, instead of grouping everything globally by technical type.

Example:

```txt
src/main/java/com/danielmaia/musiccatalog
├── artist
│   ├── domain
│   ├── repository
│   ├── dto
│   ├── mapper
│   └── service
└── common
    └── health
```

The intended request flow is:

```txt
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Each layer has a clear responsibility.

### Controller

Responsible for HTTP communication.

It receives requests, validates input, and returns responses.

It should not contain business rules.

### Service

Responsible for business rules.

Examples:

- checking if an artist already exists
- deciding whether an entity can be updated
- coordinating repository operations
- defining transactional boundaries

### Repository

Responsible for database access.

It uses Spring Data JPA to persist and query entities.

### Domain

Represents the core business entities.

The domain entity should protect its own consistency when possible.

For example, the `Artist` entity validates that the name cannot be blank and controls activation/deactivation through methods.

### DTOs

DTOs are used to separate the external API contract from the internal domain model.

The API should not expose JPA entities directly.

### Mapper

Responsible for converting domain entities into response DTOs.

---

## Database Strategy

The project uses **Flyway** for database versioning.

Hibernate is configured with:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

This means Hibernate does **not** create or update tables automatically.

Instead:

```txt
Flyway creates and updates the database schema.
Hibernate validates whether the Java entities match the database schema.
```

This approach is safer and closer to production standards.

Database migrations are stored in:

```txt
src/main/resources/db/migration
```

Current migration:

```txt
V1__create_artists_table.sql
```

---

## Profiles

The project uses different Spring profiles for different execution contexts.

### `dev`

Used when running the application locally.

This is the default profile.

Configured in:

```txt
src/main/resources/application-dev.yml
```

It uses PostgreSQL through Docker Compose.

### `test`

Used for lightweight automated tests that do not need a real database.

Configured in:

```txt
src/main/resources/application-test.yml
```

Example:

- Health controller test with MockMvc

### `integration-test`

Used for integration tests that need a real PostgreSQL database.

Configured in:

```txt
src/main/resources/application-integration-test.yml
```

These tests use Testcontainers instead of the development database.

---

## Docker Compose

The project uses Docker Compose to run PostgreSQL locally during development.

Current PostgreSQL service:

```yaml
services:
  postgres:
    image: postgres:17-alpine
    container_name: music-catalog-postgres
    environment:
      POSTGRES_DB: music_catalog
      POSTGRES_USER: music_user
      POSTGRES_PASSWORD: music_password
    ports:
      - "5433:5432"
    volumes:
      - music_catalog_postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U music_user -d music_catalog"]
      interval: 5s
      timeout: 5s
      retries: 10
```

The mapping:

```txt
5433:5432
```

means:

```txt
5433 -> port exposed on the host machine
5432 -> internal PostgreSQL port inside the container
```

This avoids conflicts with a PostgreSQL instance that may already be installed locally on port `5432`.

The Spring Boot Docker Compose integration is configured with:

```yaml
spring:
  docker:
    compose:
      enabled: true
      file: compose.yaml
      lifecycle-management: start-and-stop
```

This means that when the application starts, Spring Boot can automatically start the Docker Compose services, wait for PostgreSQL to become healthy, and then start the application.

---

## Testing Strategy

The project follows a **test-supported incremental development** approach.

The idea is to develop in small checkpoints and validate each step before moving forward.

Current test types:

### Web slice tests

Used to test controller behavior without starting the entire application.

Example:

```java
@WebMvcTest(HealthController.class)
@ActiveProfiles("test")
class HealthControllerTest {
}
```

This test uses:

- Spring MVC test context
- MockMvc
- `test` profile
- no real database
- no Docker Compose

### Repository integration tests

Used to test real persistence behavior with PostgreSQL.

Example:

```java
@DataJpaTest
@Testcontainers
@ActiveProfiles("integration-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArtistRepositoryTest {
}
```

This test uses:

- Spring Data JPA
- PostgreSQL through Testcontainers
- Flyway migrations
- real SQL execution
- isolated temporary database

The repository test does **not** use the development database from Docker Compose.

Instead, Testcontainers starts a temporary PostgreSQL container only for the test execution.

This makes the tests safer, repeatable, and independent of the local development database state.

---

## Development Workflow

This project uses a **Git Flow-inspired feature branch workflow**.

The main branches are:

```txt
main      -> stable baseline
develop   -> integration branch for completed development work
feature/* -> isolated feature branches
```

Current workflow:

```txt
Create a feature branch
        ↓
Implement a small checkpoint
        ↓
Run automated tests
        ↓
Commit the checkpoint
        ↓
Continue development
        ↓
Merge into develop only when the feature is complete
```

In practice:

```bash
git checkout develop
git checkout -b feature/artist-domain
```

Small commits are made inside the feature branch.

Example:

```bash
git commit -m "feat: add artist entity and repository"
```

The feature branch is merged into `develop` only after the feature is complete and tested.

This approach keeps `develop` cleaner and avoids merging incomplete pieces of functionality.

This is not strict Git Flow with release branches yet, but it follows the same core idea of isolating work in feature branches before integration.

---

## Current Git History

Current local history:

```txt
* feat: add artist entity and repository
* chore: configure application profiles and docker compose
* feat: add health check endpoint
* feat: create initial Spring Boot project
```

---

## How to Run Locally

### Prerequisites

Make sure the following tools are installed:

- Java 21
- Maven or Maven Wrapper
- Docker
- Docker Compose
- Git

The project includes the Maven Wrapper, so Maven does not need to be installed globally.

---

### Clone the repository

```bash
git clone git@github.com:daniel-azevedo-maia/music-catalog-api.git
cd music-catalog-api
```

---

### Run tests

```bash
./mvnw clean test
```

This command runs:

- unit/slice tests
- repository integration tests
- Testcontainers-based PostgreSQL tests

The first execution may take longer because Docker images may need to be downloaded.

---

### Run the application

```bash
./mvnw spring-boot:run
```

When running in the default `dev` profile, Spring Boot will use Docker Compose to start PostgreSQL automatically.

---

### Test the health endpoint

```bash
curl -i http://localhost:8080/api/v1/health
```

Expected response:

```json
{
  "status": "UP"
}
```

---

## Current API Endpoints

### Health Check

```http
GET /api/v1/health
```

Response:

```json
{
  "status": "UP"
}
```

More endpoints will be added as the `Artist` domain evolves.

---

## Project Structure

Current structure:

```txt
music-catalog-api
├── compose.yaml
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── danielmaia
│   │   │           └── musiccatalog
│   │   │               ├── artist
│   │   │               │   ├── domain
│   │   │               │   └── repository
│   │   │               └── common
│   │   │                   └── health
│   │   └── resources
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-test.yml
│   │       ├── application-integration-test.yml
│   │       └── db
│   │           └── migration
│   └── test
│       └── java
│           └── com
│               └── danielmaia
│                   └── musiccatalog
│                       ├── artist
│                       │   └── repository
│                       └── common
│                           └── health
```

---

## Configuration Files

### `application.yml`

Base application configuration.

Defines:

- application name
- default profile
- server port

### `application-dev.yml`

Development configuration.

Defines:

- PostgreSQL datasource
- JPA behavior
- Flyway
- Docker Compose lifecycle

### `application-test.yml`

Lightweight test configuration.

Disables:

- Docker Compose
- Flyway

Used for tests that do not require a real database.

### `application-integration-test.yml`

Integration test configuration.

Enables:

- Flyway
- JPA validation

Used together with Testcontainers.

---

## Current Branching Model

Recommended current branch usage:

```txt
main
  Stable project baseline.

develop
  Integration branch for completed features.

feature/artist-domain
  Current feature branch for the Artist domain.
```

---

## Commit Convention

This project uses a simple conventional commit style.

Examples:

```txt
feat: create initial Spring Boot project
feat: add health check endpoint
chore: configure application profiles and docker compose
feat: add artist entity and repository
```

Common prefixes:

```txt
feat   -> new feature
fix    -> bug fix
chore  -> configuration, tooling, maintenance
test   -> test-related changes
docs   -> documentation
refactor -> code improvement without changing behavior
```

This makes the Git history easier to read.

---

## Design Decisions

### Why Flyway instead of Hibernate creating tables automatically?

Because Flyway gives explicit, versioned, repeatable database evolution.

This is safer for real projects and production environments.

### Why `ddl-auto: validate`?

Because Hibernate should validate the schema, not silently create or change it.

This helps catch mismatches between entity mappings and database migrations.

### Why Testcontainers?

Because repository tests should use a real database engine.

Testing PostgreSQL behavior with PostgreSQL is more reliable than using an in-memory database that behaves differently.

### Why Docker Compose?

Because local development should be easy and reproducible.

Developers should be able to run the application without manually installing PostgreSQL.

### Why feature branches?

Because unfinished work should stay isolated until it is complete and tested.

---

## Roadmap

Planned technical improvements:

- Artist service layer
- Artist REST API
- Request and response DTOs
- Bean Validation
- Global error handling
- Pagination and sorting
- OpenAPI/Swagger documentation
- Docker image for the API
- GitHub Actions CI
- Security with Spring Security
- Authentication and authorization
- More domain models: albums, tracks, genres
- End-to-end API tests

---

## Author

Daniel Azevedo Maia

Software Engineer focused on backend development, Java, Spring Boot, cloud-native systems, and production-oriented software architecture.
