FROM openjdk:21
WORKDIR /app

# Copy JAR
COPY target/storytaleAI-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]