FROM adoptopenjdk/openjdk11

COPY build/libs/yacobooks-api-0.1.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]