FROM maven:3.9.9-eclipse-temurin-23 AS builder

WORKDIR /src

# copy files
COPY mvnw .
COPY pom.xml .

COPY .mvn .mvn
COPY src src

# make mvnw executable
RUN chmod a+x mvnw && /src/mvnw package -Dmaven.test.skip=true
# /src/target/revision-0.0.1-SNAPSHOT.jar

FROM maven:3.9.9-eclipse-temurin-23

WORKDIR /app

COPY --from=builder /src/target/vttp5_ssf_miniproject_HikeFinder-0.0.1-SNAPSHOT.jar app.jar
COPY data data

ENV PORT=8080
ENV ADMIN_USERNAME=
ENV ADMIN_PASSWORD=
ENV GOOGLE_API_KEY=

EXPOSE ${PORT}

HEALTHCHECK --interval=30s --timeout=5s --start-period=5s --retries=3 \
   CMD curl http://localhost:${PORT}/healthCheck/status || exit 1

ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar