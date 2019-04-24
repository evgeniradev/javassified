FROM openjdk:8-jdk-alpine
RUN apk add --update bash
VOLUME /tmp
ARG JAR_FILE
RUN mkdir app
WORKDIR app
COPY . /app
RUN ./mvnw clean package
