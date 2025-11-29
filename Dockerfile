# Stage 1: Build the application (Requires Java 21 JDK based on your project config)
# Using a Maven image that includes the Eclipse Temurin JDK 21
FROM maven:3.9.5-eclipse-temurin-21 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the build configuration (pom.xml) first for layer caching
COPY pom.xml .

# Copy the source code
COPY src ./src

# Command to build the executable JAR file
# Use 'package' to create the final JAR in /target and skip tests
RUN mvn clean package -DskipTests

# --- Stage 2: Create the final, lightweight runtime image ---
# Use the minimal Java 21 JRE (Runtime Environment) for a small, secure image
FROM eclipse-temurin:21-jre-jammy 

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
# Using *.jar ensures we grab the single packaged JAR, regardless of version number
COPY --from=builder /app/target/*.jar app.jar

# Expose the default Spring Boot application port (8080)
EXPOSE 8080

# Command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
