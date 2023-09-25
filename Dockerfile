FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY target/familybudget*.jar app.jar
EXPOSE 8443
EXPOSE 5432
EXPOSE 25
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
