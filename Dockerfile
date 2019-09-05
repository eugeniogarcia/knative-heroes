FROM openjdk:8-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/hero
ADD ./target/heroes-0.0.2-SNAPSHOT.jar /usr/local/hero/
CMD ./java -jar /usr/local/hero/heroes-0.0.2-SNAPSHOT.jar
