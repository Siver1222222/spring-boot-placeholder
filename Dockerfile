FROM eclipse-temurin:17-jdk as build
WORKDIR /app

# Copy the Maven POM and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests

# Create the runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Environment variables
ENV SPRING_PROFILE=prod
ENV DATABASE_URL=jdbc:postgresql://postgres:5432/uni
ENV DATABASE_USERNAME=postgres
ENV DATABASE_PASSWORD=123
ENV DDL_MODE=validate
ENV SHOW_JPA_SQL=false
ENV INIT_MODE=never

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run as non-root user for better security
RUN useradd -m appuser
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]