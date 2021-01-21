FROM alpine/git as clone
WORKDIR /app/spring-code
RUN git clone https://github.com/SKADAM1986/student-enrollment.git

FROM maven:3.5-jdk-8-alpine as build
WORKDIR /app
COPY --from=clone /app/spring-code /app

RUN mvn clean install
FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=build /app/target/EnrollmentApp-0.0.1-SNAPSHOT.jar . /app/
CMD ["java -jar -Djava.net.preferIPv4Stack=true EnrollmentApp-0.0.1-SNAPSHOT.jar"]
