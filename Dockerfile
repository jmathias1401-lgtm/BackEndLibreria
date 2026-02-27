#FROM eclipse-temurin:17-jdk-alpine
#ARG JAR_FILE=target/apinewfarma-0.0.1.jar
#COPY ${JAR_FILE} app_apinewfarma.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app_apinewfarma.jar"]

# Usar una imagen con Java 17 específicamente
#ahora se cambio aqui
FROM eclipse-temurin:17-jre AS build

WORKDIR /app

# Copiar archivos de configuración
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Descargar dependencias (cacheo)
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src src

# Compilar la aplicación
RUN ./mvnw clean package -DskipTests -Pproduction

# Imagen final
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiar el JAR construido
COPY --from=build /app/target/*.jar app.jar

# Puerto que usará la aplicación
EXPOSE 8080

# Comando de inicio
CMD ["java", "-jar", "app.jar"]