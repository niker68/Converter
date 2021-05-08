FROM adoptopenjdk/openjdk11

RUN apt-get clean && apt-get update

WORKDIR app/

COPY . .

EXPOSE 8888
ENTRYPOINT ["java","-jar","target/Converter-1.0.0-jar-with-dependencies.jar"]
