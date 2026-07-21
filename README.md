# Music Catalog API

A REST API for organizing music catalog data such as **artists, albums, tracks, and genres**.

The project provides a structured foundation for applications that need to register and maintain music metadata, expose it through HTTP endpoints, and later support features such as search, catalog analysis, and dashboards.

## Current features

- CRUD operations for artists, albums, tracks, and genres
- Artist → Album → Track relationships
- Request validation and global error handling
- Pagination and sorting on collection endpoints
- Database migrations with Flyway
- OpenAPI documentation with Swagger UI
- Unit, web, and PostgreSQL integration tests

## Tech stack

- Java 21
- Spring Boot 3.5
- Spring Data JPA and Hibernate
- PostgreSQL 17
- Flyway
- Docker Compose
- Springdoc OpenAPI
- JUnit 5, Mockito, MockMvc, AssertJ, and Testcontainers

## Domain model

```text
Artist 1 ──── N Album
Album  1 ──── N Track

Genre is currently an independent catalog resource.
```

## Requirements

- Java 21
- Docker with Docker Compose

The Maven Wrapper is included, so a local Maven installation is not required.

## Run the project

The default profile is `dev`. It connects to PostgreSQL on port `5434` and uses Spring Boot's Docker Compose integration to manage the database container.

```bash
./mvnw spring-boot:run
```

The application starts at:

```text
http://localhost:8080
```

### Run the database manually

You may also start PostgreSQL before running the application:

```bash
docker compose up -d
./mvnw spring-boot:run
```

To stop the database:

```bash
docker compose down
```

The PostgreSQL data is stored in a Docker volume and is preserved between restarts.

> The current Docker Compose file runs PostgreSQL only. The API itself currently runs through the Maven Wrapper.

## Application profiles

| Profile | Purpose |
|---|---|
| `dev` | Default local development profile. Uses PostgreSQL from `compose.yaml` and runs Flyway migrations. |
| `test` | Used by isolated automated tests. Docker Compose and Flyway are disabled. |
| `integration-test` | Used by integration tests with PostgreSQL provided by Testcontainers. |

To select a profile explicitly:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## API documentation

After starting the application, use Swagger UI to explore and test the endpoints:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI specification:

```text
http://localhost:8080/v3/api-docs
```

Health check:

```text
http://localhost:8080/api/v1/health
```

Main resources:

```text
/api/v1/artists
/api/v1/albums
/api/v1/tracks
/api/v1/genres
```

Collection endpoints support pagination and sorting. Example:

```text
GET /api/v1/tracks?page=0&size=20&sort=title,asc
```

## Run the tests

Docker must be available because repository integration tests use PostgreSQL through Testcontainers.

```bash
./mvnw test
```

## Database

Local development configuration:

```text
Database: music_catalog
Host: localhost
Port: 5434
User: music_user
Password: music_password
```

Flyway owns the database schema, while Hibernate validates that the entities match it.

## Project status

The API currently provides a documented, paginated, and tested catalog foundation. Planned improvements include search filters, genre relationships, authentication with Spring Security and JWT, full application containerization, and CI/CD.