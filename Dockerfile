FROM openjdk:8U201-jdk-alpine3.9

ENV LANG=C.UTF-8 LC_ALL=C.UTF_8

VOLUME /tmp

ADD target/mecdata-1.0-SNAPSHOT.jar app.jar

ADD target/lib lib

ENTRYPOINT ["java","-jar","/app.jar"]