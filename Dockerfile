FROM openjdk:8-jre-alpine
EXPOSE 8084
WORKDIR /app/student-enrollment
COPY target/EnrollmentApp-0.0.1-SNAPSHOT.jar .
CMD [ "java", "-jar", "-Djava.net.preferIPv4Stack=true", "EnrollmentApp-0.0.1-SNAPSHOT.jar" ]

