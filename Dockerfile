FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=target/apinewfarma-0.0.1.jar
COPY ${JAR_FILE} app_apinewfarma.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_apinewfarma.jar"]