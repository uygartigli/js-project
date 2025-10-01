# Maven image with Java 17
FROM maven:3.9.6-eclipse-temurin-17

# Install Chrome and dependencies for Selenium
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    unzip \
    curl \
    xvfb \
    && wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

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
