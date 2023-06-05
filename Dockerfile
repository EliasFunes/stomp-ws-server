FROM openjdk:17.0.2-slim-bullseye

WORKDIR /app

COPY build/libs/stomp-ws-server*.jar app.jar

ENV DB_URL=""
ENV DB_USERNAME=""
ENV DB_PASSWORD=""

ENV SECURITY_USER_PASS=""
ENV SECRET_KEY=""



EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
