FROM maven:3.8.4-openjdk-17-slim AS build
# Package the application
ADD target/sanad-*.jar /app/service.jar
# Step 2: Use the OpenJDK image with Java 17 to run the application
FROM openjdk:17-slim
# Copy the built artifact from the Maven image
ADD target/sanad-*.jar /sanad.jar

EXPOSE 8080
# Run the application
CMD ["java", "-jar", "/sanad.jar"]