FROM openjdk:20-slim

WORKDIR /app

COPY build/libs/realworld-0.0.1-SNAPSHOT.jar realworld.jar

ENTRYPOINT ["java", "-jar", "/app/realworld.jar"]
