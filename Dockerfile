FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

# Dependency cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy code
COPY src ./src
COPY testng.xml .

# Start tests
CMD ["mvn", "clean", "test"]
