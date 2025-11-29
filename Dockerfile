# Dockerfile

# Stage 1: Build the application (Uses Java 21 JDK for compilation)
FROM maven:3.9.5-eclipse-temurin-21 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml first for layer caching
COPY pom.xml .

# Copy the source code
COPY src ./src

# Command to build the executable JAR file
RUN mvn clean package -DskipTests

# --- Stage 2: Create the final, lightweight runtime image ---
# Use the minimal Java 21 JRE (Runtime Environment) for a small, secure image
FROM eclipse-temurin:21-jre-jammy 

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the default Spring Boot application port (8080)
EXPOSE 8080

# Command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
