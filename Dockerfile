# It creates a docker image base on openjdk:8-jdk-alpine, an alpine linux with openjdk8 installed.
FROM openjdk:8-jdk-alpine

# Changed the working directory to /opt/app
WORKDIR /opt/app

# Copy demo-0.0.1-SNAPSHOT.jar to /opt/app/app.jar
ARG JAR_FILE=target/demo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Run the jar file with ENTRYPOINT
ENTRYPOINT ["java","-jar","app.jar"]
