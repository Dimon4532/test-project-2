# ЭТАП 1: Сборка (Build stage)
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY contracts ./contracts
COPY producer-service ./producer-service
COPY consumer-service ./consumer-service
COPY validator-service ./validator-service
COPY analytics-service ./analytics-service

# Собираем только нужные модули
RUN mvn clean install -pl contracts -am -DskipTests
RUN mvn clean package -pl analytics-service -am -DskipTests

# ЭТАП 2: Запуск (Run stage)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Копируем собранный jar-файл из этапа сборки
COPY --from=build /app/analytics-service/target/analytics-service-1.0-SNAPSHOT.jar app.jar

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]