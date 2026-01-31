FROM eclipse-temurin:17-jdk-alpine

EXPOSE 8080

ADD target/orders-1.0-SNAPSHOT.jar orders-1.0-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "orders-1.0-SNAPSHOT.jar"]