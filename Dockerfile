# Stage 1: Build the Spring Boot application
FROM maven:3.8.4-openjdk-17 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project descriptor and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the application source code
COPY src ./src

# Build the application JAR file
RUN mvn package -DskipTests

# Stage 2: Create a minimal runtime image
FROM postgres:latest

# Install OpenJDK 17 in the PostgreSQL image
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    openjdk-17-jdk \
    postgresql-client && \
    rm -rf /var/lib/apt/lists/*



# Use the built JAR file from the builder stage
COPY --from=builder /app/target/assessment-0.0.1.jar /app/assessment.jar

# Expose the port that the Spring Boot application will run on
EXPOSE 8090

# Command to run the Spring Boot application
CMD ["java", "-jar", "/app/assessment.jar"]
