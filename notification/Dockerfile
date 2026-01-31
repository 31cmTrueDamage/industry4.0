FROM eclipse-temurin:17-jdk-alpine

EXPOSE 8080

ADD target/notification-1.0-SNAPSHOT.jar notification-1.0-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "notification-1.0-SNAPSHOT.jar"]