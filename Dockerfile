FROM openjdk:8u181-jre-stretch

RUN mkdir /app
WORKDIR /app

ARG JAR_FILE
COPY ${JAR_FILE} working-day-service.jar

ENV JAVA_OPTS="-Xmx2048m"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar working-day-service.jar" ]