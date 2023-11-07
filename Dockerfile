FROM openjdk:8-jre-alpine
COPY target/*.jar ski.jar
EXPOSE 8080
CMD ["java", "-jar", "ski.jar"]
