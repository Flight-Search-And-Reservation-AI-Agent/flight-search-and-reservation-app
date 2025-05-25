# Stage 1: Build the Spring Boot app using Maven
FROM maven:3.8.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven files and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the app using a lightweight JDK image
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
