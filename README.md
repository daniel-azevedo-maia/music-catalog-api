# Music Catalog API

A production-oriented REST API built with **Java 21**, **Spring Boot**, **PostgreSQL**, **Flyway**, **JPA/Hibernate**, **Docker Compose**, and automated tests.

This project simulates the development of a real backend system for managing a music catalog. It was built step by step using professional backend practices such as versioned database migrations, layered architecture, DTO-based API contracts, input validation, global exception handling, feature branches, automated testing, and manual API validation.

The goal of this project is not just to expose CRUD endpoints. The goal is to demonstrate how a backend application can be designed, tested, documented, and evolved in a maintainable way.

---

## Project Overview

Music Catalog API is a backend system for managing structured music catalog data.

The current version supports:

- Artist management
- Album management
- Track management
- Relationship between artists, albums, and tracks
- Input validation
- Structured error responses
- Automated tests across controller, service, and repository layers
- Manual validation through HTTP requests with `curl`

The project is organized as a practical portfolio backend application, with emphasis on clean code, testability, database consistency, incremental delivery, and production-oriented decisions.

Current domain relationship:

```txt
Artist 1 ---- N Album
Album  1 ---- N Track
```

Meaning:

```txt
One artist can have many albums.
One album can have many tracks.
One track belongs to one album.
```

---

## Current Status

The project is under active development.

Implemented so far:

- Initial Spring Boot project setup
- Health check endpoint
- Environment configuration with Spring profiles
- PostgreSQL local environment with Docker Compose
- Versioned database migrations with Flyway
- JPA/Hibernate configuration with schema validation
- Artist domain
- Artist CRUD REST API
- Album domain
- Album CRUD REST API
- Track domain
- Track CRUD REST API
- Relationship `Album -> Artist`
- Relationship `Track -> Album`
- DTOs for requests and responses
- Mapper classes
- Service layer with transactional boundaries
- Global exception handling
- Controller tests with MockMvc
- Service tests with Mockito
- Repository integration tests with Testcontainers
- Manual API validation with `curl`

Last automated test result:

```txt
Tests run: 39, Failures: 0, Errors: 0, Skipped: 0
```

---

## Technical Highlights

- Java 21 and Spring Boot 3.5.15
- Clean layered architecture
- Domain-oriented package organization
- PostgreSQL 17 with Docker Compose
- Flyway migrations instead of automatic schema generation
- `ddl-auto: validate` to prevent accidental schema changes
- DTO-based API contracts
- Bean Validation for request validation
- Global exception handler with consistent JSON error responses
- Testcontainers for PostgreSQL integration tests
- MockMvc for web layer testing
- Mockito for service layer unit tests
- Git feature branch workflow
- Manual validation with `curl`

---

## Technology Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.15 |
| Build Tool | Maven Wrapper |
| Web Layer | Spring Web |
| Persistence | Spring Data JPA |
| ORM | Hibernate |
| Database | PostgreSQL 17 |
| Migrations | Flyway |
| Validation | Jakarta Bean Validation |
| Boilerplate Reduction | Lombok |
| Local Infrastructure | Docker Compose |
| Testing | JUnit 5 |
| Mocking | Mockito |
| Assertions | AssertJ |
| Web Testing | MockMvc |
| Integration Testing | Testcontainers |
| Version Control | Git |

---

## Implemented Domains

### Artist

Represents a music artist.

Main fields:

```txt
id
name
biography
country
active
createdAt
updatedAt
```

Artists use soft deletion. Instead of being physically removed from the database, they are marked as inactive.

Current endpoints:

```http
GET    /api/v1/artists
GET    /api/v1/artists/{id}
POST   /api/v1/artists
PUT    /api/v1/artists/{id}
DELETE /api/v1/artists/{id}
```

---

### Album

Represents a music album.

Main fields:

```txt
id
title
releaseDate
artist
createdAt
updatedAt
```

Current relationship:

```txt
Artist 1 ---- N Album
```

Meaning:

```txt
One artist can have many albums.
One album belongs to one main artist.
```

Current endpoints:

```http
GET    /api/v1/albums
GET    /api/v1/albums/{id}
POST   /api/v1/albums
PUT    /api/v1/albums/{id}
DELETE /api/v1/albums/{id}
```

---

### Track

Represents a song inside an album.

Main fields:

```txt
id
title
durationSeconds
trackNumber
album
createdAt
updatedAt
```

Current relationship:

```txt
Album 1 ---- N Track
```

Meaning:

```txt
One album can have many tracks.
One track belongs to one album.
```

Current endpoints:

```http
GET    /api/v1/tracks
GET    /api/v1/tracks/{id}
POST   /api/v1/tracks
PUT    /api/v1/tracks/{id}
DELETE /api/v1/tracks/{id}
```

The `TrackResponse` also includes album and artist information, making the API response more useful for clients:

```txt
track -> album -> artist
```

Example response fields:

```txt
id
title
durationSeconds
trackNumber
albumId
albumTitle
artistId
artistName
createdAt
updatedAt
```

---

## Architecture

The project follows a layered architecture organized by domain.

Main request flow:

```txt
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Layer responsibilities:

```txt
Controller -> Handles HTTP requests, validation, and API responses
Service    -> Contains business rules and transaction boundaries
Repository -> Handles database access through Spring Data JPA
Domain     -> Represents business entities and internal consistency rules
DTO        -> Defines the external API contract
Mapper     -> Converts domain entities to response DTOs
```

Main package structure:

```txt
src/main/java/com/danielmaia/musiccatalog
├── album
│   ├── controller
│   ├── domain
│   ├── dto
│   ├── mapper
│   ├── repository
│   └── service
├── artist
│   ├── controller
│   ├── domain
│   ├── dto
│   ├── mapper
│   ├── repository
│   └── service
├── track
│   ├── controller
│   ├── domain
│   ├── dto
│   ├── mapper
│   ├── repository
│   └── service
└── common
    ├── exception
    └── health
```

---

## Database Strategy

The project uses **Flyway** for database schema versioning.

Hibernate is configured with:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

This means:

```txt
Flyway creates and updates the database schema.
Hibernate only validates whether the Java mappings match the database schema.
```

This approach is safer and closer to real production environments than allowing Hibernate to create or update tables automatically.

Current migrations:

```txt
V1__create_artists_table.sql
V2__create_albums_table.sql
V3__create_tracks_table.sql
```

Migration directory:

```txt
src/main/resources/db/migration
```

Current tables:

```txt
artists
albums
tracks
```

---

## Spring Profiles

### dev

Default profile for local development.

Configuration file:

```txt
src/main/resources/application-dev.yml
```

Uses PostgreSQL through Docker Compose.

---

### test

Used for lightweight automated tests, mainly controller tests.

Configuration file:

```txt
src/main/resources/application-test.yml
```

Does not use the development database or Docker Compose.

---

### integration-test

Used for repository integration tests with a real PostgreSQL database.

Configuration file:

```txt
src/main/resources/application-integration-test.yml
```

Uses Testcontainers to create a temporary PostgreSQL container for test execution.

---

## Docker Compose

The project uses Docker Compose to run PostgreSQL locally.

PostgreSQL service:

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
      - "5434:5432"
    volumes:
      - music_catalog_postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U music_user -d music_catalog"]
      interval: 5s
      timeout: 5s
      retries: 10

volumes:
  music_catalog_postgres_data:
```

Port mapping:

```txt
5434:5432
```

Meaning:

```txt
5434 -> host machine port
5432 -> PostgreSQL internal container port
```

The project uses host port `5434` to avoid conflicts with other local databases or containers using ports `5432` or `5433`.

---

## How to Run Locally

### Prerequisites

Install:

```txt
Java 21
Docker
Docker Compose
Git
```

The project includes the Maven Wrapper, so Maven does not need to be installed globally.

---

### Clone the repository

```bash
git clone git@github.com:daniel-azevedo-maia/music-catalog-api.git
cd music-catalog-api
```

---

### Run the automated tests

```bash
./mvnw clean test
```

This command runs:

```txt
Controller tests
Service tests
Repository integration tests with Testcontainers
```

---

### Run the application

```bash
./mvnw spring-boot:run
```

By default, the application uses the `dev` profile.

The API runs at:

```txt
http://localhost:8080
```

---

## API Endpoints

### Health

```http
GET /api/v1/health
```

---

### Artists

```http
GET    /api/v1/artists
GET    /api/v1/artists/{id}
POST   /api/v1/artists
PUT    /api/v1/artists/{id}
DELETE /api/v1/artists/{id}
```

---

### Albums

```http
GET    /api/v1/albums
GET    /api/v1/albums/{id}
POST   /api/v1/albums
PUT    /api/v1/albums/{id}
DELETE /api/v1/albums/{id}
```

---

### Tracks

```http
GET    /api/v1/tracks
GET    /api/v1/tracks/{id}
POST   /api/v1/tracks
PUT    /api/v1/tracks/{id}
DELETE /api/v1/tracks/{id}
```

---

## Manual API Testing with curl

### Health check

```bash
curl -i http://localhost:8080/api/v1/health
```

---

### Create an artist

```bash
curl -i -X POST http://localhost:8080/api/v1/artists \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Queen",
    "biography": "British rock band.",
    "country": "United Kingdom"
  }'
```

---

### Create an album

Use the `id` returned by the artist creation request.

```bash
curl -i -X POST http://localhost:8080/api/v1/albums \
  -H "Content-Type: application/json" \
  -d '{
    "title": "A Night at the Opera",
    "releaseDate": "1975-11-21",
    "artistId": 1
  }'
```

---

### Create a track

Use the `id` returned by the album creation request.

```bash
curl -i -X POST http://localhost:8080/api/v1/tracks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Bohemian Rhapsody",
    "durationSeconds": 354,
    "trackNumber": 11,
    "albumId": 1
  }'
```

---

### List tracks

```bash
curl -i http://localhost:8080/api/v1/tracks
```

---

### Find track by ID

```bash
curl -i http://localhost:8080/api/v1/tracks/1
```

---

### Update a track

```bash
curl -i -X PUT http://localhost:8080/api/v1/tracks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Bohemian Rhapsody - Remastered",
    "durationSeconds": 355,
    "trackNumber": 11
  }'
```

---

### Delete a track

```bash
curl -i -X DELETE http://localhost:8080/api/v1/tracks/1
```

---

## Validated API Behavior

### Album

Album scenarios manually validated:

```txt
Create album with blank title          -> 400
Create album without artistId          -> 400
Create album with non-existing artist  -> 404
Find deleted album                     -> 404
```

Example album validation error:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/albums",
  "fieldErrors": {
    "title": "Album title is required"
  }
}
```

Example album not found error:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Album not found",
  "path": "/api/v1/albums/1",
  "fieldErrors": {}
}
```

---

### Track

Track scenarios manually validated:

```txt
GET    /api/v1/health                         -> 200
POST   /api/v1/tracks                         -> 201
GET    /api/v1/tracks                         -> 200
GET    /api/v1/tracks/{id}                    -> 200
PUT    /api/v1/tracks/{id}                    -> 200
POST   /api/v1/tracks with blank title        -> 400
POST   /api/v1/tracks with invalid duration   -> 400
POST   /api/v1/tracks with invalid number     -> 400
POST   /api/v1/tracks with non-existing album -> 404
DELETE /api/v1/tracks/{id}                    -> 204
GET    /api/v1/tracks/{id} after delete       -> 404
```

Example track validation error:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/tracks",
  "fieldErrors": {
    "title": "Track title is required"
  }
}
```

Example track not found error:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Track not found",
  "path": "/api/v1/tracks/1",
  "fieldErrors": {}
}
```

---

## Testing Strategy

The project uses multiple test levels.

### Controller tests

Used to test the web layer with MockMvc.

Main tools:

```txt
@WebMvcTest
@MockitoBean
MockMvc
test profile
```

Validated concerns:

```txt
HTTP status codes
Response JSON
Request validation
Service interaction
```

Examples:

```txt
ArtistControllerTest
AlbumControllerTest
TrackControllerTest
HealthControllerTest
```

---

### Service tests

Used to test business logic with mocked dependencies.

Main tools:

```txt
JUnit 5
Mockito
AssertJ
@Mock
@InjectMocks
```

Validated concerns:

```txt
Create operations
Find operations
List operations
Update operations
Delete operations
Not found errors
Service-repository interaction
```

Examples:

```txt
ArtistServiceImplTest
AlbumServiceImplTest
TrackServiceImplTest
```

---

### Repository integration tests

Used to test real persistence behavior with PostgreSQL.

Main tools:

```txt
@DataJpaTest
@Testcontainers
PostgreSQLContainer
Flyway
integration-test profile
```

These tests do not use the local development database.

Each test execution creates an isolated temporary PostgreSQL container.

Examples:

```txt
ArtistRepositoryTest
AlbumRepositoryTest
TrackRepositoryTest
```

---

## Development Workflow

The project uses a Git Flow-inspired workflow.

Main branches:

```txt
main      -> stable baseline
develop   -> integration branch for completed features
feature/* -> isolated feature development
```

Feature workflow:

```txt
Update develop
Create feature branch
Plan the feature
Create migration
Create entity
Create repository
Create repository tests
Create DTOs
Create mapper
Create service
Create service tests
Create controller
Create controller tests
Run automated tests
Validate manually with curl
Update README/tutorial
Merge into develop
```

---

## Recent Git History

Main commits from `artist-domain`:

```txt
feat: add artist entity and repository
feat: add artist service layer
feat: add artist list endpoint
feat: add artist CRUD endpoints
fix: update artist timestamp immediately
feat: add global exception handling
```

Main commits from `album-domain`:

```txt
feat: add album entity and repository
feat: add album service layer
feat: add album REST endpoints
chore: change dev postgres port
docs: update project readme
```

Main commits from `track-domain`:

```txt
feat: add track entity and repository
feat: add track service layer
feat: add track REST endpoints
```

---

## Commit Convention

The project uses a simple convention based on Conventional Commits.

Examples:

```txt
feat: add track REST endpoints
feat: add album REST endpoints
fix: update artist timestamp immediately
chore: change dev postgres port
docs: update project readme
test: add album controller tests
refactor: improve album service implementation
```

Common prefixes:

```txt
feat     -> new feature
fix      -> bug fix
chore    -> maintenance or configuration
docs     -> documentation
test     -> tests
refactor -> internal improvement without behavior change
```

---

## Technical Decisions

### Why Flyway?

Because database changes should be explicit, versioned, and reproducible.

---

### Why `ddl-auto: validate`?

Because Hibernate should not silently create or modify the database schema in a production-oriented project.

---

### Why DTOs?

Because the API contract should be separated from the internal JPA entities.

The API does not expose entities directly.

---

### Why Testcontainers?

Because repository tests should run against a real PostgreSQL database instead of an in-memory database with different behavior.

---

### Why Docker Compose?

Because local development should be reproducible without manually installing PostgreSQL.

---

### Why feature branches?

Because incomplete work should remain isolated until it is implemented, tested, and validated.

---

### Why manual validation with curl?

Because automated tests are necessary, but running the application and validating real HTTP behavior is also useful before merging a completed feature.

---

## Roadmap

Possible next steps:

```txt
Pagination and sorting
OpenAPI/Swagger documentation
Genre domain
Search artists
Search albums
Search tracks
Filters by country, year, album, artist, and status
Dockerfile for the API
GitHub Actions CI pipeline
Spring Boot Actuator
Prometheus and Grafana observability
Structured logs
Spring Security authentication
Role-based authorization
Cloud deployment
```

---

## Author

Daniel Azevedo Maia

Software Engineer focused on backend development, Java, Spring Boot, REST APIs, cloud-native systems, and production-oriented software architecture.