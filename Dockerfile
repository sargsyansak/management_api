FROM maven:3.6.3-jdk-8

WORKDIR /app
ADD . .
RUN mvn clean install -DskipTests 
CMD ["java", "-jar", "target/spring-boot-docker-0.0.1-SNAPSHOT.jar"]
