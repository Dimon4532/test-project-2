# Project Structure And Run Guide

## 1. Назначение проекта

`test-project-2` это multi-module Java/Spring Boot проект с событийной архитектурой.
Основной сценарий:

1. `producer-service` принимает и сохраняет данные сотрудников.
2. `producer-service` публикует событие в Kafka.
3. `validator-service` валидирует сообщение по JSON Schema.
4. Валидное сообщение уходит дальше в Kafka, невалидное попадает в DLQ.
5. `consumer-service` читает валидные события и сохраняет их в свою БД.
6. `analytics-service` собирает и агрегирует метрики, сохраняет их в MongoDB и отдает через REST/WebSocket.

## 2. Структура репозитория

```text
test-project-2/
├── pom.xml
├── docker-compose.yml
├── README.md
├── PROJECT_STRUCTURE.md
├── contracts/
├── producer-service/
├── validator-service/
├── consumer-service/
└── analytics-service/
```

## 3. Модули и их ответственность

### `contracts`

Общий модуль с контрактами между сервисами.

- `src/main/java/ru/learning/java/kafka/Topics.java`
  Содержит имена Kafka topics:
  - `company.employee.raw`
  - `company.employee.validated`
  - `company.employee.dlq`
- `src/main/java/ru/learning/java/model/EmployeeEvent.java`
  Базовая модель события.
- `src/main/resources/schemas/employee-with-department-event-v1.schema.json`
  JSON Schema для валидации событий.

Этот модуль подключается как зависимость в `producer-service`, `validator-service` и `analytics-service`.

### `producer-service`

Основной сервис для работы с сотрудниками.

Технологии:
- Spring Web
- Spring Data JPA
- PostgreSQL
- Flyway
- Spring Kafka
- Spring Data Elasticsearch

Ключевые части:
- `controller/EmployeeController.java`
  REST API для CRUD-подобных операций и бизнес-действий.
- `controller/EmployeeSearchController.java`
  REST API для поиска сотрудников в Elasticsearch.
- `service/EmployeeService.java`
  Бизнес-логика по найму, обновлению зарплаты, переиндексации и демо-операциям.
- `service/EmployeeEventProducer.java`
  Публикация Kafka-события после найма сотрудника.
- `repository/EmployeeRepository.java`, `EmployeeJpaRepository.java`
  Работа с PostgreSQL.
- `repository/EmployeeSearchRepository.java`
  Работа с Elasticsearch.
- `resources/db/migration`
  Миграции Flyway.

Что хранит:
- PostgreSQL: основная модель сотрудников.
- Elasticsearch: поисковый индекс сотрудников.

Что отдает наружу:
- HTTP API на порту `8080`.
- Kafka-события о сотрудниках.

### `validator-service`

Промежуточный сервис в Kafka pipeline.

Технологии:
- Spring Boot
- Spring Kafka
- JSON Schema Validator

Ключевые части:
- `validator/kafka/EmployeeEventValidatorListener.java`
  Слушает raw topic, валидирует и маршрутизирует сообщения.
- `validator/kafka/JsonSchemaValidator.java`
  Загружает JSON Schema и выполняет валидацию.
- `validator/kafka/DlqMessageFactory.java`
  Формирует сообщение для DLQ.

Что делает:
- Читает raw события из Kafka.
- Валидирует payload по схеме.
- Валидные сообщения отправляет в validated topic.
- Невалидные сообщения отправляет в DLQ topic.

### `consumer-service`

Сервис-получатель валидных Kafka-событий.

Технологии:
- Spring Web
- Spring Data JPA
- PostgreSQL
- Flyway
- Spring Kafka

Ключевые части:
- `service/EmployeeEventConsumer.java`
  Слушает Kafka и сохраняет событие в БД.
- `controller/EmployeeRecordController.java`
  Отдает сохраненные записи через REST API.
- `entity/EmployeeRecord.java`
  JPA-сущность для таблицы `employee_records`.
- `repository/EmployeeRepository.java`
  Spring Data репозиторий.

Что хранит:
- Отдельную PostgreSQL БД с журналом полученных событий.

Что отдает наружу:
- HTTP API на порту `8083`.

### `analytics-service`

Сервис аналитики и мониторинга.

Технологии:
- Spring Web
- Spring Kafka
- Kafka Streams
- MongoDB
- WebSocket/STOMP
- Caffeine Cache
- Spring Actuator + Prometheus

Ключевые части:
- `service/MetricsCollector.java`
  Слушает Kafka и собирает оперативные метрики по сервисам.
- `scheduler/AnalyticsScheduler.java`
  Периодически сохраняет накопленные метрики в MongoDB.
- `service/AnalyticsService.java`
  Строит агрегированные отчеты по сохраненным метрикам.
- `controller/AnalyticsController.java`
  REST API аналитики.
- `service/NotificationService.java`
  Публикация обновлений в WebSocket.
- `config/WebSocketConfig.java`
  Настройка endpoint `/ws-analytics` и broker `/topic`.
- `config/KafkaStreamsConfig.java`
  Пример потоковой обработки и агрегации событий по статусам.
- `repository/MetricsRepository.java`
  MongoDB repository.
- `resources/static/dashboard.html`
  Простая web-страница для dashboard.

Что хранит:
- MongoDB коллекцию `service_metrics`.

Что отдает наружу:
- HTTP API на порту `8084`.
- Dashboard: `http://localhost:8084/dashboard.html`
- WebSocket endpoint: `/ws-analytics`
- Prometheus metrics: `/actuator/prometheus`

## 4. Взаимосвязь модулей

### На уровне зависимостей Maven

- root `pom.xml` агрегирует все модули.
- `contracts` используется как общий библиотечный модуль.
- `producer-service`, `validator-service`, `analytics-service` явно зависят от `contracts`.
- `consumer-service` работает со своей локальной DTO-моделью события и не зависит от `contracts`.

### На уровне инфраструктуры

- `producer-service` зависит от:
  - Kafka
  - PostgreSQL `producer_db`
  - Elasticsearch
- `validator-service` зависит от:
  - Kafka
  - схемы в собственных ресурсах
- `consumer-service` зависит от:
  - Kafka
  - PostgreSQL `consumer_db`
- `analytics-service` зависит от:
  - Kafka
  - MongoDB

### На уровне потока данных

```text
HTTP client
   |
   v
producer-service
   | \
   |  \-> PostgreSQL (producer_db)
   | 
   \----> Elasticsearch
   |
   \----> Kafka raw topic
             |
             v
      validator-service
         |         \
         |          \-> Kafka DLQ topic
         v
   Kafka validated topic
         |
         v
   consumer-service
         |
         v
  PostgreSQL (consumer_db)

Kafka topics
   |
   v
analytics-service
   | \
   |  \-> MongoDB
   |  \-> REST API
   \-> WebSocket dashboard
```

## 5. Основные точки входа

### Producer API

Базовый префикс: `/api/v1/employees`

- `GET /api/v1/employees`
- `POST /api/v1/employees`
- `GET /api/v1/employees/average-salary`
- `POST /api/v1/employees/work-routine`
- `POST /api/v1/employees/demo-concurrency`
- `PUT /api/v1/employees/{id}/salary?newSalary=...`

Поисковый API:

- `GET /api/v1/employees/search/by-name?name=...`
- `GET /api/v1/employees/search/by-department?department=...`
- `GET /api/v1/employees/search/by-type?type=...`
- `GET /api/v1/employees/search/all`
- `POST /api/v1/employees/search/reindex`

### Consumer API

Базовый префикс: `/api/employees`

- `GET /api/employees`
- `GET /api/employees/{id}`
- `GET /api/employees/search?name=...`
- `GET /api/employees/status/{status}`
- `GET /api/employees/count`

### Analytics API

Базовый префикс: `/api/analytics`

- `GET /api/analytics/report?hours=24`
- `GET /api/analytics/metrics/{serviceName}?hours=24`
- `GET /api/analytics/health`

Дополнительно:

- `GET /dashboard.html`
- `GET /actuator/prometheus`
- WebSocket/SockJS endpoint: `/ws-analytics`

## 6. Инфраструктура из `docker-compose.yml`

`docker-compose.yml` поднимает:

- Kafka
- PostgreSQL для producer: `localhost:5432`
- PostgreSQL для consumer: `localhost:5433`
- Elasticsearch: `localhost:9200`
- MongoDB: `localhost:27017`
- все 4 Spring Boot сервиса

Если запускать сервисы локально из IDE или через Maven, compose можно использовать только для инфраструктуры.

## 7. Инструкция по запуску

### Вариант A. Запуск приложения полностью через Docker Compose

1. Убедиться, что установлены:
   - Docker
   - Docker Compose
2. Из корня проекта выполнить:

```bash
docker-compose up -d --build
```

3. Проверить доступность сервисов:
   - `http://localhost:8080/api/v1/employees`
   - `http://localhost:8083/api/employees`
   - `http://localhost:8084/api/analytics/health`
   - `http://localhost:8084/dashboard.html`

4. Остановить окружение:

```bash
docker-compose down
```

Если нужно удалить volume-данные:

```bash
docker-compose down -v
```

### Вариант B. Локальный запуск сервисов через Maven

Требования:
- JDK 21
- Maven 3.x
- Docker и Docker Compose для инфраструктурных сервисов

1. Поднять только инфраструктуру:

```bash
docker-compose up -d kafka postgres-producer postgres-consumer elasticsearch mongodb
```

2. Собрать проект из корня:

```bash
mvn clean install
```

3. Запустить сервисы по отдельности:

```bash
mvn -pl producer-service spring-boot:run
```

```bash
mvn -pl validator-service spring-boot:run
```

```bash
mvn -pl consumer-service spring-boot:run
```

```bash
mvn -pl analytics-service spring-boot:run
```

4. Порты локального запуска:
- `producer-service`: `8080`
- `consumer-service`: `8083`
- `analytics-service`: `8084`

### Рекомендуемый порядок старта

1. Kafka
2. PostgreSQL для producer
3. PostgreSQL для consumer
4. Elasticsearch
5. MongoDB
6. `producer-service`
7. `validator-service`
8. `consumer-service`
9. `analytics-service`

## 8. Конфигурация по сервисам

### Producer

- Kafka: `localhost:9092`
- PostgreSQL: `jdbc:postgresql://localhost:5432/producer_db`
- Elasticsearch: `http://localhost:9200`

### Consumer

- Kafka: `localhost:9092`
- PostgreSQL: `jdbc:postgresql://localhost:5433/consumer_db`
- HTTP port: `8083`

### Validator

- Kafka: `localhost:9092`

### Analytics

- Kafka: `localhost:9092`
- MongoDB: `mongodb://localhost:27017/analytics_db`
- HTTP port: `8084`

## 9. Тесты

Запуск всех тестов:

```bash
mvn test
```

В проекте используются unit tests и integration tests. Для части интеграционных тестов нужны Docker/Testcontainers.

## 10. Важные замечания по текущему состоянию проекта

При чтении кода видны расхождения, которые важно учитывать при запуске и отладке:

1. Имена Kafka topics не везде согласованы.
   - В `contracts` объявлены:
     - `company.employee.raw`
     - `company.employee.validated`
     - `company.employee.dlq`
   - В `producer-service` raw topic публикуется через `Topics.EMPLOYEE_RAW`, то есть в `company.employee.raw`.
   - В `validator-service` validated topic тоже берется из `Topics.EMPLOYEE_VALIDATED`, то есть `company.employee.validated`.
   - Но `consumer-service` слушает hardcoded topic `validated-employees`.
   - `analytics-service` слушает hardcoded topics `employee-events` и `validated-employees`.

   Это означает, что в текущем виде цепочка сервисов выглядит логически правильно, но в runtime сообщения могут не дойти до `consumer-service` и `analytics-service`, если topics не будут приведены к одному набору имен.

2. В `producer-service` есть класс `service/EmployeeEventConsumer.java`, который слушает Kafka, но не помечен `@Service` и по структуре не участвует в основном сценарии сервиса. Его можно рассматривать как экспериментальный или вспомогательный код.

3. Проверка сборки в текущем окружении не выполнялась.
   - В рабочем окружении, где готовился этот документ, команда `mvn` отсутствовала.
   - Maven wrapper (`mvnw`) в репозитории не найден.
   - Поэтому инструкция запуска основана на конфигурации проекта, `README.md`, `pom.xml` и `docker-compose.yml`, а не на фактически выполненной локальной сборке.

## 11. Что полезно читать в первую очередь

Если нужно быстро понять проект, лучше идти в таком порядке:

1. `pom.xml`
2. `docker-compose.yml`
3. `contracts/src/main/java/ru/learning/java/kafka/Topics.java`
4. `producer-service/src/main/java/ru/learning/java/controller/EmployeeController.java`
5. `producer-service/src/main/java/ru/learning/java/service/EmployeeEventProducer.java`
6. `validator-service/src/main/java/ru/learning/java/validator/kafka/EmployeeEventValidatorListener.java`
7. `consumer-service/src/main/java/ru/learning/java/service/EmployeeEventConsumer.java`
8. `analytics-service/src/main/java/ru/learning/java/service/MetricsCollector.java`
9. `analytics-service/src/main/java/ru/learning/java/controller/AnalyticsController.java`
