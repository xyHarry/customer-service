# Start with an official Java runtime as a parent image
FROM eclipse-temurin:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the Maven build artifact (JAR file) into the container
COPY target/customer-service-0.0.1-SNAPSHOT.jar customer-service.jar

# Expose the port that the Spring Boot application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "customer-service.jar"]
