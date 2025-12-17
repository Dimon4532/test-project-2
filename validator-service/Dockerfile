# ЭТАП 1: Сборка (Build stage)
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY contracts ./contracts
COPY producer-service ./producer-service
COPY validator-service ./validator-service

# Собираем только нужные модули
RUN mvn clean install -pl contracts -am -DskipTests
RUN mvn clean package -pl validator-service -am -DskipTests

# ЭТАП 2: Запуск (Run stage)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Копируем собранный jar-файл из этапа сборки
COPY --from=build /app/validator-service/target/validator-service-1.0-SNAPSHOT.jar app.jar

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]