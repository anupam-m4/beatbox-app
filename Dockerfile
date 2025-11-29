# Stage 1: Build the application using Maven and Java 17
# We use a Maven image that includes the JDK necessary for compiling.
FROM maven:3.9.5-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the build configuration (pom.xml) first to leverage Docker layer caching
COPY pom.xml .

# Copy the source code
COPY src ./src

# Command to build jar
RUN mvn clean package -DskipTests

# --- Stage 2: Create the final, lightweight runtime image ---
FROM eclipse-temurin:21-jdk-jammy

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
# NOTE: The JAR file name typically follows the pattern: artifactId-version.jar
# Please verify that 'beatBoxapi-0.0.1-SNAPSHOT.jar' is the correct file name in your 'target' directory.
COPY --from=builder /app/target/*.jar app.jar

# Expose the default Spring Boot application port (8080)
EXPOSE 8080

# Command to run the application when the container starts
# The 'java -jar app.jar' command executes the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
