FROM eclipse-temurin:23-jre
LABEL authors="femrek"

WORKDIR /app
COPY target/*.jar app.jar
COPY docker-entrypoint.sh .

ENTRYPOINT ["sh", "docker-entrypoint.sh"]
