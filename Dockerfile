FROM adoptopenjdk/openjdk11:jdk-11.0.5_10-alpine
WORKDIR /app
COPY target/familybudget*.jar app.jar
EXPOSE 8080
EXPOSE 5432
EXPOSE 25
ENTRYPOINT ["java", "-jar", "app.jar"]

#FROM adoptopenjdk/openjdk11:jdk-11.0.5_10-alpine
#ADD . /src
#WORKDIR /src
#RUN chmod +x ./mvnw
#RUN ./mvnw package -DskipTests
#EXPOSE 8080
#EXPOSE 5432
#ENTRYPOINT ["java","-jar","target/microservices-backend-1.0.0.jar"]

#FROM maven:3.9.1-openjdk-11-slim AS builder
#WORKDIR /app
#COPY pom.xml .
#RUN mvn -e -B dependency:resolve
#COPY src ./src
#RUN mvn clean -e -B package



# RTSDK Java
#FROM openjdk:11-jre-slim-buster
#WORKDIR /app
#COPY --from=builder /app/target/microservices-backend-1.0.0.jar .
#COPY EmaConfig.xml .
#COPY etc ./etc
#EXPOSE 8080
#EXPOSE 5432
# run microservices-backend-1.0.0.jar with CMD
#CMD ["java", "-jar", "./microservices-backend-1.0.0.jar"]

