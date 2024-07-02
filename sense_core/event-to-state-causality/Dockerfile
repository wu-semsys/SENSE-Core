FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /opt/event-to-state-causality

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package

FROM openjdk:20-ea-11-jdk-slim


COPY --from=build /opt/event-to-state-causality/target/Event-to-State-Causality-1.0-SNAPSHOT.jar ./opt/event-to-state-causality/event-to-state-causality.jar


WORKDIR /opt/event-to-state-causality

CMD ["java", "-jar", "event-to-state-causality.jar", "config.json"]
