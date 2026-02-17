# Test Project 2

A multi-module Spring Boot system demonstrating a distributed architecture with Kafka messaging, PostgreSQL persistence,
and Elasticsearch search capabilities.

## Overview

The system consists of several microservices communicating via Kafka and utilizing various storage technologies:

- **Contracts**: Shared data models and schemas.
- **Producer Service**: Handles employee data, persists to PostgreSQL, and indexes to Elasticsearch. Sends events to
  Kafka.
- **Validator Service**: Validates messages using JSON Schema.
- **Consumer Service**: Consumes messages from Kafka and persists data to its own PostgreSQL database.

## Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.5.10
- **Package Manager**: Maven
- **Messaging**: Apache Kafka
- **Databases**: PostgreSQL 16, Elasticsearch 8.12.2
- **Infrastructure**: Docker & Docker Compose

## Requirements

- JDK 21
- Maven 3.x
- Docker and Docker Compose

## Project Structure

```text
test-project-2/
├── contracts/           # Shared models and schemas
├── producer-service/    # Producer application (Postgres + ES + Kafka)
├── validator-service/   # Validator application (Kafka + Schema validation)
├── consumer-service/    # Consumer application (Postgres + Kafka)
├── docker-compose.yml   # Infrastructure setup (Kafka, Postgres, ES)
└── pom.xml              # Root Maven configuration
```

## Setup & Run

### 1. Start Infrastructure

Use Docker Compose to start Kafka, PostgreSQL (2 instances), and Elasticsearch:

```bash
docker-compose up -d
```

### 2. Build the Project

Build all modules using Maven:
`mvn clean install`

### 3. Run Services

You can run each service using the Spring Boot Maven plugin:

**Producer Service:**
`mvn -pl producer-service spring-boot:run`

- Port: `8080`

**Consumer Service:**
`mvn -pl consumer-service spring-boot:run`

- Port: `8083`

**Validator Service:**
`mvn -pl validator-service spring-boot:run`

## Configuration

### Key Environment Variables / Properties

Default values are configured in `application.properties` of each service. They can be overridden via environment
variables:

| Property                           | Default (Local)                                | Docker Compose Value                                   |
|------------------------------------|------------------------------------------------|--------------------------------------------------------|
| `SPRING_KAFKA_BOOTSTRAP_SERVERS`   | `localhost:9092`                               | `kafka:29092`                                          |
| `SPRING_DATASOURCE_URL` (Producer) | `jdbc:postgresql://localhost:5432/producer_db` | `jdbc:postgresql://postgres-producer:5432/producer_db` |
| `SPRING_DATASOURCE_URL` (Consumer) | `jdbc:postgresql://localhost:5433/consumer_db` | `jdbc:postgresql://postgres-consumer:5432/consumer_db` |
| `SPRING_ELASTICSEARCH_URIS`        | `http://localhost:9200`                        | `http://elasticsearch:9200`                            |

## Scripts & Entry Points

- **Build**: `mvn clean install`
- **Run Tests**: `mvn test`
- **Infrastructure**: `docker-compose up -d` / `docker-compose down`

## Tests

To run all tests across all modules:
`mvn test`

The project uses **Testcontainers** for integration testing. Ensure Docker is running when executing tests.

## TODO / Unknowns

- [ ] Add API documentation (e.g., Swagger/OpenAPI).
- [ ] Implement centralized logging/monitoring.
- [ ] Define CI/CD pipeline.
- [ ] TODO: Verify authentication/authorization requirements.

## License

[Insert License Information Here - e.g., MIT, Apache 2.0]
