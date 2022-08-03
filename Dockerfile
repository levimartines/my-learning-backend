FROM amazoncorretto:17
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} service.jar
ENTRYPOINT ["java", "-jar", "/service.jar"]