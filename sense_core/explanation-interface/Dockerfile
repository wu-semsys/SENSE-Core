FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /opt/explanation-interface

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package

FROM openjdk:20-ea-11-jdk-slim


COPY --from=build /opt/explanation-interface/target/Explanation-Interface-1.0-SNAPSHOT.jar ./opt/explanation-interface/explanation-interface.jar


WORKDIR /opt/explanation-interface

CMD ["java", "-jar", "explanation-interface.jar"]
