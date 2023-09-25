FROM adoptopenjdk/openjdk11:jdk-11.0.5_10-alpine
WORKDIR /app
COPY target/familybudget*.jar app.jar
EXPOSE 8080
EXPOSE 5432
EXPOSE 25
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
