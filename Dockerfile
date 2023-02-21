FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
EXPOSE 8080
COPY ./target/marketour-0.0.1-SNAPSHOT.jar marketour.jar
ENTRYPOINT ["java","-jar","/marketour.jar"]