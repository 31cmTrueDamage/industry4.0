FROM eclipse-temurin:17-jdk-alpine

EXPOSE 8080

ADD target/inventory-1.0-SNAPSHOT.jar inventory-1.0-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "inventory-1.0-SNAPSHOT.jar"]