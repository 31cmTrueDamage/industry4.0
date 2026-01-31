FROM eclipse-temurin:17-jdk-alpine

EXPOSE 8080

ADD target/production-1.0-SNAPSHOT.jar production-1.0-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "production-1.0-SNAPSHOT.jar"]