FROM amazoncorretto:17
ADD build/libs/*.jar service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/service.jar"]