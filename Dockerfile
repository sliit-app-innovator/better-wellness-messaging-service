# Use Amazon Corretto 17 Alpine as the base image
FROM public.ecr.aws/amazoncorretto/amazoncorretto:17

# Set working directory inside the container
WORKDIR /app

# Copy Spring Boot JAR file into the container
COPY target/better-wellness-messaging-service-1.0.0.jar app.jar

# Copy application.properties into the same directory
COPY src/main/resources/application.properties .

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application directly
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=./application.properties"]
