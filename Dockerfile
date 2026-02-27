#FROM eclipse-temurin:17-jdk-alpine
#ARG JAR_FILE=target/apinewfarma-0.0.1.jar
#COPY ${JAR_FILE} app_apinewfarma.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app_apinewfarma.jar"]

# Etapa de build - NECESITA JDK para compilar
# Etapa de build
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

# Etapa final - con diagnóstico
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Instalar herramientas de diagnóstico (opcional)
RUN apk add --no-cache curl busybox-extras

# Copiar el JAR construido
COPY --from=build /app/target/*.jar app.jar

# Crear script de inicio con diagnóstico
RUN echo '#!/bin/sh' > /app/start.sh && \
    echo 'echo "========================================="' >> /app/start.sh && \
    echo 'echo "🚀 INICIANDO CONTENEDOR - $(date)"' >> /app/start.sh && \
    echo 'echo "========================================="' >> /app/start.sh && \
    echo 'echo "📂 Directorio actual: $(pwd)"' >> /app/start.sh && \
    echo 'echo "📋 Contenido del directorio:"' >> /app/start.sh && \
    echo 'ls -la >> /app/start.log 2>&1' >> /app/start.sh && \
    echo 'echo ""' >> /app/start.sh && \
    echo 'echo "☕ Versión de Java:"' >> /app/start.sh && \
    echo 'java -version >> /app/start.log 2>&1' >> /app/start.sh && \
    echo 'echo ""' >> /app/start.sh && \
    echo 'echo "🌍 Variables de entorno:"' >> /app/start.sh && \
    echo 'env | grep -E "PORT|SECRET|JWT|DB" >> /app/start.log 2>&1' >> /app/start.sh && \
    echo 'echo ""' >> /app/start.sh && \
    echo 'echo "🚀 EJECUTANDO APLICACIÓN"' >> /app/start.sh && \
    echo 'echo "========================================="' >> /app/start.sh && \
    echo 'exec java -jar app.jar --server.port=${PORT:-8080}' >> /app/start.sh && \
    chmod +x /app/start.sh

# Exponer puerto
EXPOSE 8080

# Ejecutar script de inicio
CMD ["/app/start.sh"]