FROM maven:3.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the Maven POM and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests

# Create the runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Environment variables
ENV SPRING_PROFILE=prod
ENV DATABASE_URL=jdbc:postgresql://noroff-postgres.postgres.database.azure.com:5432/uni?createDatabaseIfNotExist=true&user=postgres&password=Pt@123456&sslmode=require
ENV DDL_MODE=create
ENV DB_SEED_MODE=never
ENV SHOW_JPA_SQL=true
ENV INIT_MODE=always

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run as non-root user for better security
RUN useradd -m appuser
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]