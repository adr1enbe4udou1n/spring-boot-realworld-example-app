FROM openjdk:11-jre-slim

COPY build/libs/realworld-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
