FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package

FROM openjdk:20-ea-11-jdk-slim

COPY --from=build /app/target/Event-to-State-Causality-1.0-SNAPSHOT.jar ./app.jar

CMD ["java", "-jar", "app.jar"]
