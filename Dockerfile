FROM adoptopenjdk/openjdk11

RUN apt-get clean && apt-get update

WORKDIR /app

COPY target/*.jar /app/Converter-1.0-SNAPSHOT.jar

EXPOSE 8888
ENTRYPOINT ["java","-jar","/app/Converter-1.0-SNAPSHOT.jar"]
