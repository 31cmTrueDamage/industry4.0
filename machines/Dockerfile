FROM eclipse-temurin:17-jdk-alpine

EXPOSE 8080

ADD target/machines-1.0-SNAPSHOT.jar machines-1.0-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "machines-1.0-SNAPSHOT.jar"]