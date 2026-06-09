# ==========================================
# STAGE 1: Build the Application
# ==========================================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# 1. Copy pom.xml first to leverage Docker layer caching for dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copy source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# ==========================================
# STAGE 2: Run the Application (Production)
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 3. Copy only the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# 4. Expose port 8080 (Standard for Spring Boot)
EXPOSE 8080

# 5. Run with memory optimization flags for containers
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", "app.jar"]

## Run the application
## The API key will be passed via Environment Variables in Render/AWS
#ENTRYPOINT ["java", "-jar", "app.jar"]