# Test Project 2

A multi-module Spring Boot system demonstrating a distributed architecture with Kafka messaging, PostgreSQL persistence,
Elasticsearch search capabilities, and real-time analytics.

Extended project documentation:
- `PROJECT_STRUCTURE.md` - detailed structure, module relationships, and run guide

## Overview

The system consists of several microservices communicating via Kafka and utilizing various storage technologies:

- **Contracts**: Shared data models and schemas.
- **Producer Service**: Handles employee data, persists to PostgreSQL, and indexes to Elasticsearch. Sends events to
  Kafka.
- **Validator Service**: Validates messages using JSON Schema.
- **Consumer Service**: Consumes messages from Kafka and persists data to its own PostgreSQL database.
- **Analytics Service**: 📊 Collects metrics from all services, provides real-time analytics, and exposes a web
  dashboard.

## Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.5.10
- **Package Manager**: Maven
- **Messaging**: Apache Kafka (+ Kafka Streams)
- **Databases**: PostgreSQL 16, Elasticsearch 8.12.2, MongoDB 7.0
- **Cache**: Caffeine
- **Real-time**: WebSocket (STOMP)
- **Monitoring**: Micrometer + Prometheus
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
├── analytics-service/   # Analytics application (MongoDB + Kafka Streams + WebSocket)
├── docker-compose.yml   # Infrastructure setup
└── pom.xml              # Root Maven configuration
```

## Setup & Run

### 1. Start Infrastructure

Use Docker Compose to start Kafka, PostgreSQL (2 instances), Elasticsearch, and MongoDB:

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

**Analytics Service:** 📊
`mvn -pl analytics-service spring-boot:run`

- Port: `8084`
- Dashboard: `http://localhost:8084/dashboard.html`

## Analytics Service Features

### 🎯 Key Capabilities:

- **Real-time Metrics Collection** via Kafka listeners
- **Stream Processing** using Kafka Streams
- **Aggregated Analytics** with scheduled persistence
- **Caching** for performance optimization (Caffeine)
- **WebSocket** for live dashboard updates
- **REST API** for programmatic access
- **MongoDB** for flexible metrics storage
- **Prometheus Metrics** for monitoring

### 📡 API Endpoints:

| Endpoint                                    | Method    | Description                      |
|---------------------------------------------|-----------|----------------------------------|
| `/api/analytics/report?hours=24`            | GET       | Get aggregated analytics report  |
| `/api/analytics/metrics/{service}?hours=24` | GET       | Get metrics for specific service |
| `/api/analytics/health`                     | GET       | Health check                     |
| `/actuator/prometheus`                      | GET       | Prometheus metrics               |
| `/ws-analytics`                             | WebSocket | Real-time metrics stream         |

### 📊 Dashboard Features:

- Real-time event counters per service
- Success/failure rates
- System health score
- Interactive timeline chart
- WebSocket auto-reconnection

## Configuration

### Key Environment Variables / Properties

| Property                           | Default (Local)                                | Docker Compose Value                                   |
|------------------------------------|------------------------------------------------|--------------------------------------------------------|
| `SPRING_KAFKA_BOOTSTRAP_SERVERS`   | `localhost:9092`                               | `kafka:29092`                                          |
| `SPRING_DATASOURCE_URL` (Producer) | `jdbc:postgresql://localhost:5432/producer_db` | `jdbc:postgresql://postgres-producer:5432/producer_db` |
| `SPRING_DATASOURCE_URL` (Consumer) | `jdbc:postgresql://localhost:5433/consumer_db` | `jdbc:postgresql://postgres-consumer:5432/consumer_db` |
| `SPRING_ELASTICSEARCH_URIS`        | `http://localhost:9200`                        | `http://elasticsearch:9200`                            |
| `SPRING_DATA_MONGODB_URI`          | `mongodb://localhost:27017/analytics_db`       | `mongodb://mongodb:27017/analytics_db`                 |

## Scripts & Entry Points

- **Build**: `mvn clean install`
- **Run Tests**: `mvn test`
- **Infrastructure**: `docker-compose up -d` / `docker-compose down`
- **View Logs**: `docker-compose logs -f analytics-service`

## Tests

To run all tests across all modules:

`mvn test`

The project uses **Testcontainers** for integration testing with Kafka and MongoDB. Ensure Docker is running when
executing tests.

### Analytics Service Tests:

- ✅ Unit tests with Mockito
- ✅ Integration tests with Testcontainers (Kafka + MongoDB)
- ✅ WebSocket connection tests
- ✅ Metrics aggregation tests

## Architecture Highlights

### Data Flow:

```
Producer → Kafka (employee-events) → Validator → Kafka (validated-employees) → Consumer
                     ↓                                       ↓
              Analytics Service ← ← ← ← ← ← ← ← ← ← ← ← ← ←
                     ↓
              [MongoDB Storage]
                     ↓
              [WebSocket Push] → Dashboard (Real-time UI)
```

### Technologies Demonstrated:

- ✅ **Microservices Architecture**
- ✅ **Event-Driven Design** (Kafka)
- ✅ **Stream Processing** (Kafka Streams)
- ✅ **Polyglot Persistence** (PostgreSQL, MongoDB, Elasticsearch)
- ✅ **Caching Strategies** (Caffeine)
- ✅ **Real-time Communication** (WebSocket)
- ✅ **Scheduled Tasks** (@Scheduled)
- ✅ **Metrics & Monitoring** (Micrometer + Prometheus)
- ✅ **Integration Testing** (Testcontainers)
- ✅ **Dockerization**

## TODO / Future Enhancements

- [ ] Add Grafana dashboards for Prometheus metrics
- [ ] Implement alert system for critical failures
- [ ] Add historical trend analysis
- [ ] Implement data retention policies
- [ ] Add authentication for dashboard
- [ ] Create CI/CD pipeline
- [ ] Add API documentation (Swagger/OpenAPI)

## License

[Insert License Information Here - e.g., MIT, Apache 2.0]

```
