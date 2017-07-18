FROM openjdk

ENV GRAPHITE_URL="graphite.local"

COPY target/*.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]