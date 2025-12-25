FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY build/libs/realworld-0.0.1-SNAPSHOT.jar realworld.jar

ENTRYPOINT ["java", "-jar", "/app/realworld.jar"]
