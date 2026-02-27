#FROM eclipse-temurin:17-jdk-alpine
#ARG JAR_FILE=target/apinewfarma-0.0.1.jar
#COPY ${JAR_FILE} app_apinewfarma.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app_apinewfarma.jar"]

# Etapa de build - NECESITA JDK para compilar
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copiar archivos de configuración
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Dar permisos y descargar dependencias
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente y compilar
COPY src src
RUN ./mvnw clean package -DskipTests

# Etapa final - SOLO JRE necesario para ejecutar
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR construido
COPY --from=build /app/target/*.jar app.jar

# Crear usuario no root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Exponer puerto
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]