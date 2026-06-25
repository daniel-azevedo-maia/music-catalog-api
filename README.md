# Music Catalog API

A REST API built with **Java 21**, **Spring Boot**, **PostgreSQL**, **Flyway**, **JPA/Hibernate**, **Docker Compose**, and automated tests.

This project is being developed step by step as a practical backend portfolio project. The goal is not only to create CRUD endpoints, but to show how a real backend API can be designed, implemented, tested, documented, and evolved in an organized way.

The project currently supports:

- Artists
- Albums
- Tracks
- Genres
- Validation
- Global error handling
- Automated tests
- Manual API validation with `curl`

---

## Project Goal

The main goal of this project is to practice and demonstrate a complete backend development workflow using Java and Spring Boot.

The project is intentionally being built in small, clear steps:

```txt
1. Create the database migration
2. Create the domain entity
3. Create the repository
4. Test the repository with a real PostgreSQL container
5. Create DTOs
6. Create the mapper
7. Create the service
8. Test the service with Mockito
9. Create the controller
10. Test the controller with MockMvc
11. Validate manually with curl
12. Update the README
13. Merge the feature branch
```

This approach keeps each feature easy to understand, test, review, and maintain.

---

## Current Status

Implemented so far:

```txt
Health check endpoint
Artist domain
Album domain
Track domain
Genre domain
Global exception handling
Request validation
Layered architecture
Database migrations with Flyway
Repository tests with Testcontainers
Service tests with Mockito
Controller tests with MockMvc
Manual validation with curl
```

Current domain relationships:

```txt
Artist 1 ---- N Album
Album  1 ---- N Track
```

The `Genre` domain currently exists as an independent catalog entity.

A future step may connect genres to tracks or albums.

---

## Technology Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.15 |
| Build Tool | Maven Wrapper |
| Web | Spring Web |
| Persistence | Spring Data JPA |
| ORM | Hibernate |
| Database | PostgreSQL 17 |
| Migrations | Flyway |
| Validation | Jakarta Bean Validation |
| Boilerplate Reduction | Lombok |
| Local Infrastructure | Docker Compose |
| Testing | JUnit 5 |
| Assertions | AssertJ |
| Mocking | Mockito |
| Web Tests | MockMvc |
| Integration Tests | Testcontainers |
| Version Control | Git |

---

## Architecture

The project uses a simple layered architecture.

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

Each layer has a clear responsibility:

```txt
Controller -> Receives HTTP requests and returns HTTP responses
Service    -> Contains application/business logic and transaction boundaries
Repository -> Communicates with the database through Spring Data JPA
Domain     -> Represents the internal model of the application
DTO        -> Defines what enters and leaves the API
Mapper     -> Converts domain objects into response DTOs
```

---

## Package Structure

Main structure:

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
├── common
│   ├── exception
│   └── health
├── genre
│   ├── controller
│   ├── domain
│   ├── dto
│   ├── mapper
│   ├── repository
│   └── service
└── track
    ├── controller
    ├── domain
    ├── dto
    ├── mapper
    ├── repository
    └── service
```

The project is organized by domain instead of by technical layer only. This keeps each feature easier to locate and evolve.

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

Artists use soft deletion.

Instead of being physically deleted from the database, they are marked as inactive.

Endpoints:

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

Relationship:

```txt
Artist 1 ---- N Album
```

Meaning:

```txt
One artist can have many albums.
One album belongs to one artist.
```

Endpoints:

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

Relationship:

```txt
Album 1 ---- N Track
```

Meaning:

```txt
One album can have many tracks.
One track belongs to one album.
```

The track response also includes album and artist information:

```txt
track -> album -> artist
```

Endpoints:

```http
GET    /api/v1/tracks
GET    /api/v1/tracks/{id}
POST   /api/v1/tracks
PUT    /api/v1/tracks/{id}
DELETE /api/v1/tracks/{id}
```

---

### Genre

Represents a music genre.

Main fields:

```txt
id
name
description
createdAt
updatedAt
```

Examples:

```txt
Rock
Jazz
Pop
MPB
Classical
Hip Hop
```

Endpoints:

```http
GET    /api/v1/genres
GET    /api/v1/genres/{id}
POST   /api/v1/genres
PUT    /api/v1/genres/{id}
DELETE /api/v1/genres/{id}
```

Implemented in the Genre feature:

```txt
V4__create_genres_table.sql
Genre entity
GenreRepository
GenreRepositoryTest
GenreCreateRequest
GenreUpdateRequest
GenreResponse
GenreMapper
GenreService
GenreServiceImpl
GenreServiceImplTest
GenreController
GenreControllerTest
Manual validation with curl
```

---

## Database Strategy

The project uses **Flyway** for database migrations.

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
Hibernate only validates whether the Java entities match the database schema.
```

This is closer to production than allowing Hibernate to create or update tables automatically.

Current migrations:

```txt
V1__create_artists_table.sql
V2__create_albums_table.sql
V3__create_tracks_table.sql
V4__create_genres_table.sql
```

Current tables:

```txt
artists
albums
tracks
genres
```

Migration directory:

```txt
src/main/resources/db/migration
```

---

## Validation Strategy

The project uses validation in three different levels.

### 1. DTO validation

DTOs validate incoming HTTP requests.

Example:

```java
public record GenreCreateRequest(

        @NotBlank(message = "Genre name is required")
        @Size(max = 80, message = "Genre name must have at most 80 characters")
        String name,

        @Size(max = 255, message = "Genre description must have at most 255 characters")
        String description
) {
}
```

This prevents invalid requests from reaching the service layer.

---

### 2. Domain protection

Entities protect their own internal consistency.

Example:

```java
public Genre(String name, String description) {
    this.name = requireText(name, "Genre name is required");
    this.description = normalizeOptionalText(description);
}
```

This protects the domain even if the entity is created outside the controller flow.

---

### 3. Database constraints

The database is the final integrity barrier.

Example:

```sql
CREATE TABLE genres (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_genres_name
        UNIQUE (name)
);
```

So the project uses:

```txt
DTO validation       -> validates API input
Entity validation    -> protects the domain model
Database constraints -> protect persisted data
```

---

## Error Handling

The project has global exception handling.

Validation errors return structured JSON responses.

Example:

```json
{
  "timestamp": "2026-06-25T18:45:32.800002666Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/genres",
  "fieldErrors": {
    "name": "Genre name is required"
  }
}
```

Not found errors also return structured JSON responses.

Example:

```json
{
  "timestamp": "2026-06-25T18:45:47.393756494Z",
  "status": 404,
  "error": "Not Found",
  "message": "Genre not found",
  "path": "/api/v1/genres/1",
  "fieldErrors": {}
}
```

---

## Testing Strategy

The project uses three main kinds of tests:

```txt
Controller tests
Service tests
Repository integration tests
```

Each type of test has a different purpose.

---

### Controller Tests

Controller tests validate the HTTP layer.

Main tools:

```txt
@WebMvcTest
@MockitoBean
MockMvc
jsonPath
status matchers
```

Example structure:

```java
@WebMvcTest(GenreController.class)
@ActiveProfiles("test")
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenreService genreService;
}
```

What is being tested:

```txt
HTTP status codes
Request validation
JSON response fields
Controller-to-service interaction
```

Example:

```java
mockMvc.perform(post("/api/v1/genres")
                .contentType("application/json")
                .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Rock"));
```

This means:

```txt
Perform a POST request to /api/v1/genres.
Expect HTTP 201 Created.
Expect the JSON response to contain id = 1.
Expect the JSON response to contain name = Rock.
```

The service is mocked in controller tests because the goal is not to test business logic here. The goal is to test the web layer.

---

### Service Tests

Service tests validate the application logic.

Main tools:

```txt
JUnit 5
Mockito
AssertJ
@Mock
@InjectMocks
when
thenReturn
thenAnswer
verify
ReflectionTestUtils
```

Example structure:

```java
@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImpl genreService;
}
```

Meaning:

```txt
@Mock creates a fake GenreRepository.
@InjectMocks creates the real GenreServiceImpl and injects the fake repository into it.
```

So the service is real, but the repository is fake.

This is important because a service unit test should test the service logic without depending on a real database.

---

### `when(...).thenReturn(...)`

Used to define what a mock should return.

Example:

```java
when(genreRepository.findById(1L))
        .thenReturn(Optional.of(genre));
```

Meaning:

```txt
When genreRepository.findById(1L) is called,
return Optional.of(genre).
```

This is necessary because the repository is a mock. It does not know what to return unless we define it.

---

### `when(...).thenAnswer(...)`

Used when the mock response depends on the argument passed to the method.

Example:

```java
when(genreRepository.save(any(Genre.class)))
        .thenAnswer(invocation -> saveGenreWithId(invocation.getArgument(0), 1L));
```

Meaning:

```txt
When genreRepository.save(...) is called,
get the Genre passed to save,
set id = 1,
and return that same Genre.
```

This simulates what the database normally does.

In real execution:

```txt
The service creates a Genre without id.
The repository saves it.
The database generates the id.
The repository returns the saved Genre with id.
```

In the unit test:

```txt
The service creates a Genre without id.
The mocked repository intercepts save(...).
The test manually sets id = 1.
The mocked repository returns the Genre with id.
```

---

### `verify(...)`

Used to check whether a mock method was called.

Example:

```java
verify(genreRepository).save(any(Genre.class));
```

Meaning:

```txt
Verify that genreRepository.save(...) was called with any Genre object.
```

Important:

```txt
This does not verify that something was saved in the database.
There is no real database in a service unit test.
It only verifies that the service called the repository correctly.
```

Database persistence is tested in repository integration tests.

---

### `assertThat(...)`

Used to check the result returned by the method being tested.

Example:

```java
assertThat(response.id()).isEqualTo(1L);
assertThat(response.name()).isEqualTo("Rock");
```

Meaning:

```txt
Check that the service returned a response with id = 1.
Check that the service returned a response with name = Rock.
```

---

### `assertThatThrownBy(...)`

Used to test exceptions.

Example:

```java
assertThatThrownBy(() -> genreService.findById(99L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Genre not found");
```

Meaning:

```txt
When genreService.findById(99L) is called,
expect an EntityNotFoundException
with the message Genre not found.
```

---

### `ReflectionTestUtils`

Used in tests to set private fields such as `id`.

Example:

```java
ReflectionTestUtils.setField(genre, "id", 1L);
```

Why this is used:

```txt
The id is private.
There is no public setter for id.
The id is generated by the database in real execution.
In a unit test, there is no real database.
So the test manually sets the id to simulate a saved entity.
```

This keeps the production code clean while still allowing tests to simulate persisted entities.

---

### Repository Integration Tests

Repository tests validate real persistence behavior.

Main tools:

```txt
@DataJpaTest
@Testcontainers
PostgreSQLContainer
@ServiceConnection
Flyway
AssertJ
```

Example structure:

```java
@DataJpaTest
@Testcontainers
@ActiveProfiles("integration-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenreRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private GenreRepository genreRepository;
}
```

Meaning:

```txt
@DataJpaTest loads only the JPA-related parts of Spring.
@Testcontainers enables temporary Docker containers for tests.
PostgreSQLContainer starts a real PostgreSQL database just for the test.
@ServiceConnection connects Spring automatically to that test database.
```

This is where the project verifies actual database behavior.

Example:

```java
Genre genre = new Genre("Rock", "Music genre characterized by electric guitars, drums and strong rhythm.");

Genre savedGenre = genreRepository.saveAndFlush(genre);

assertThat(savedGenre.getId()).isNotNull();
assertThat(savedGenre.getName()).isEqualTo("Rock");
```

Meaning:

```txt
Create a Genre.
Save it in a real PostgreSQL test database.
Check that the database generated an id.
Check that the values were persisted correctly.
```

---

## What Each Test Type Proves

```txt
Controller test   -> the HTTP API behaves correctly
Service test      -> the business/application logic behaves correctly
Repository test   -> the database persistence behaves correctly
```

They do not replace each other.

They complement each other.

---

## Manual API Testing with curl

Automated tests are important, but the project also validates features manually with `curl`.

This confirms that the application runs and responds through real HTTP requests.

---

### Health

```bash
curl -i http://localhost:8080/api/v1/health
```

Expected:

```txt
200 OK
```

---

### Create Genre

```bash
curl -i -X POST http://localhost:8080/api/v1/genres \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Rock",
    "description": "Music genre characterized by electric guitars, drums and strong rhythm."
  }'
```

Expected:

```txt
201 Created
```

---

### List Genres

```bash
curl -i http://localhost:8080/api/v1/genres
```

Expected:

```txt
200 OK
```

---

### Find Genre by ID

```bash
curl -i http://localhost:8080/api/v1/genres/1
```

Expected:

```txt
200 OK
```

---

### Update Genre

```bash
curl -i -X PUT http://localhost:8080/api/v1/genres/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Classic Rock",
    "description": "Rock music from the classic era."
  }'
```

Expected:

```txt
200 OK
```

---

### Invalid Genre

```bash
curl -i -X POST http://localhost:8080/api/v1/genres \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "description": "Invalid genre."
  }'
```

Expected:

```txt
400 Bad Request
```

---

### Genre Not Found

```bash
curl -i http://localhost:8080/api/v1/genres/999
```

Expected:

```txt
404 Not Found
```

---

### Delete Genre

```bash
curl -i -X DELETE http://localhost:8080/api/v1/genres/1
```

Expected:

```txt
204 No Content
```

---

### Confirm Genre Was Deleted

```bash
curl -i http://localhost:8080/api/v1/genres/1
```

Expected:

```txt
404 Not Found
```

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

The project uses Maven Wrapper, so Maven does not need to be installed globally.

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

This runs:

```txt
Controller tests
Service tests
Repository integration tests
```

---

### Run the application

```bash
./mvnw spring-boot:run
```

The API runs at:

```txt
http://localhost:8080
```

---

## Docker Compose

The project uses Docker Compose for the local PostgreSQL database.

PostgreSQL configuration:

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

---

## Spring Profiles

### dev

Used for local development.

```txt
src/main/resources/application-dev.yml
```

---

### test

Used for controller tests and lightweight tests.

```txt
src/main/resources/application-test.yml
```

---

### integration-test

Used for repository tests with Testcontainers.

```txt
src/main/resources/application-integration-test.yml
```

---

## Git Workflow

The project uses feature branches.

Main branches:

```txt
main
develop
feature/*
```

Feature workflow:

```txt
1. Start from develop
2. Create a feature branch
3. Implement the feature in small steps
4. Commit each major checkpoint
5. Push to GitHub
6. Run all tests
7. Validate manually with curl
8. Update README
9. Merge into develop
```

Example feature branches:

```txt
feature/artist-domain
feature/album-domain
feature/track-domain
feature/genre-domain
```

---

## Recent Feature Checkpoints

### Track Domain

```txt
feat: add track entity and repository
feat: add track service layer
feat: add track REST endpoints
docs: update track feature tutorial
```

---

### Genre Domain

```txt
feat: add genre entity and repository
feat: add genre service layer
feat: add genre REST endpoints
```

---

## Commit Convention

The project uses a simple Conventional Commits style.

Examples:

```txt
feat: add genre REST endpoints
feat: add track service layer
docs: update genre feature tutorial
fix: update artist timestamp immediately
chore: change dev postgres port
test: add genre controller tests
refactor: improve service implementation
```

Common prefixes:

```txt
feat     -> new feature
fix      -> bug fix
docs     -> documentation
test     -> tests
chore    -> maintenance/configuration
refactor -> internal improvement without changing behavior
```

---

## Roadmap

Possible next steps:

```txt
Connect Genre to Track or Album
Pagination and sorting
Search endpoints
Swagger/OpenAPI documentation
Improve exception handling
Structured logs
Spring Boot Actuator
Prometheus and Grafana observability
Dockerfile for the API
GitHub Actions CI
Spring Security
JWT authentication
Cloud deployment
```

---

## Author

Daniel Azevedo Maia

Software Engineer focused on backend development, Java, Spring Boot, REST APIs, cloud-native systems, and production-oriented software architecture.
