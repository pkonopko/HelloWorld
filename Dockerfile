FROM openjdk:17-jdk-alpine
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY target/ShortnerURL-0.0.1-SNAPSHOT.jar app.jar
RUN chown -R app:app /app
USER app
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]