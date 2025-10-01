# Maven image with Java 17
FROM maven:3.9.6-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Copy pom.xml, src, testng.xml, config.properties and simplelogger.properties
COPY pom.xml .
COPY src ./src
COPY testng.xml .
COPY src/test/resources/config.properties ./src/test/resources/config.properties
COPY src/test/resources/simplelogger.properties ./src/test/resources/simplelogger.properties

# Download dependencies
RUN mvn dependency:go-offline -B
# Create reports directory structure
RUN mkdir -p /app/reports/html-reports

# Start tests with proper exit code handling
CMD ["mvn", "clean", "test", "-Dmaven.test.failure.ignore=false"]
