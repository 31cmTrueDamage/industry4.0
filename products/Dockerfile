FROM eclipse-temurin:17-jdk-alpine

EXPOSE 8080

ADD target/products-1.0-SNAPSHOT.jar products-1.0-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "products-1.0-SNAPSHOT.jar"]