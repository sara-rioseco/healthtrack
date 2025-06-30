FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/healthtrack-platform-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]