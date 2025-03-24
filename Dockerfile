# Use Amazon Corretto 17 Alpine as the base image
FROM amazoncorretto:17.0.8-alpine3.18

# Set working directory inside the container
WORKDIR /app

# Copy Spring Boot JAR file into the container
COPY target/better-wellness-messaging-service-1.0.0.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application directly
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
