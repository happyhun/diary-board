FROM eclipse-temurin:17

LABEL maintainer="younghun.ai@gmail.com"

ARG JAR_FILE=./build/libs/*-SNAPSHOT.jar

COPY ${JAR_FILE} diary-board.jar

ENTRYPOINT ["java","-jar","diary-board.jar"]