# Use an official minimal Java runtime image. 
# (If you need JDK features at runtime, use eclipse-temurin:17-jdk-alpine.)
FROM eclipse-temurin:23-jre-alpine

# Create an app directory inside the container
WORKDIR /app

# Copy the JAR into the container
COPY hospital-app.jar /app/app.jar

# Expose the port your app runs on (optional, for documentation)
EXPOSE 8080

# Provide default environment variable values (adjust as needed)
ENV DB_HOST=db
ENV DB_PORT=3306
ENV DB_NAME=hospitaldb
ENV DB_USER=hospitaluser
ENV DB_PASS=hospitalpass

# Run the JAR
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
