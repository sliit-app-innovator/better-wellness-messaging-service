# Use Amazon Corretto 17 Alpine as the base image
FROM amazoncorretto:17.0.8-alpine3.18

# Set working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/better-wellness-user-api-1.0.0.jar app.jar

# Copy entrypoint script and make it executable
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Expose the application port (adjust if needed)
EXPOSE 8080

# Run the entrypoint script
ENTRYPOINT ["/entrypoint.sh"]
