FROM openjdk:8u201-jdk-alpine3.9

ADD target/mecdata-1.0-SNAPSHOT.jar app.jar

ADD target/lib lib

ENTRYPOINT ["java","-jar","/app.jar","-Xms128m -Xmx128m"]

