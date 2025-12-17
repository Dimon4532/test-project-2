# ЭТАП 1: Сборка (Build stage)
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY contracts ./contracts
COPY producer-service ./producer-service

RUN mvn clean install -pl contracts -am -DskipTests
RUN mvn clean package -pl producer-service -am -DskipTests

# ЭТАП 2: Запуск (Run stage)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/producer-service/target/producer-service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]