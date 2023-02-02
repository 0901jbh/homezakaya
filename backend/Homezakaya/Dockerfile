FROM openjdk:11-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=Homezakaya-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","/app.jar"]