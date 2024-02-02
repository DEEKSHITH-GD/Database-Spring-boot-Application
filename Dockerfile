FROM openjdk:17
WORKDIR /app
ARG JAR_FILE=target/Database-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]