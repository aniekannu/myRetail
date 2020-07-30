FROM openjdk:11

ADD  build/libs/myRetail-0.0.1-SNAPSHOT.jar *.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "*.jar"]