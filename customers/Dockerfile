FROM eclipse-temurin:17-jdk-alpine

EXPOSE 8080

ADD target/customers-1.0-SNAPSHOT.jar customers-1.0-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "customers-1.0-SNAPSHOT.jar"]