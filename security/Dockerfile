FROM maven:3.8.6-jdk-11-slim AS builder
WORKDIR /src
ADD pom.xml pom.xml
ADD src src/
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:11-slim as deploy
WORKDIR /app
COPY --from=builder /src/target/*.jar app.jar
EXPOSE 8080
CMD ["java","-jar","/app/app.jar"]
