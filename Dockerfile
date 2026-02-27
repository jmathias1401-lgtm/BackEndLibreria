#FROM eclipse-temurin:17-jdk-alpine
#ARG JAR_FILE=target/apinewfarma-0.0.1.jar
#COPY ${JAR_FILE} app_apinewfarma.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app_apinewfarma.jar"]

# Etapa de build - NECESITA JDK para compilar
# Etapa de build
# Etapa final - con diagnóstico en consola
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Instalar herramientas de diagnóstico (opcional)
RUN apk add --no-cache curl

# Copiar el JAR construido
COPY --from=build /app/target/*.jar app.jar

# Crear script de inicio con diagnóstico en consola
RUN echo '#!/bin/sh' > /app/start.sh && \
    echo 'echo "========================================="' >> /app/start.sh && \
    echo 'echo "🚀 INICIANDO CONTENEDOR - $(date)"' >> /app/start.sh && \
    echo 'echo "========================================="' >> /app/start.sh && \
    echo 'echo "📂 Directorio actual: $(pwd)"' >> /app/start.sh && \
    echo 'echo "📋 Contenido del directorio:"' >> /app/start.sh && \
    echo 'ls -la' >> /app/start.sh && \
    echo 'echo ""' >> /app/start.sh && \
    echo 'echo "☕ Versión de Java:"' >> /app/start.sh && \
    echo 'java -version 2>&1' >> /app/start.sh && \
    echo 'echo ""' >> /app/start.sh && \
    echo 'echo "🌍 Variables de entorno:"' >> /app/start.sh && \
    echo 'env | grep -E "PORT|SECRET|JWT|DB" || echo "No hay variables JWT configuradas"' >> /app/start.sh && \
    echo 'echo ""' >> /app/start.sh && \
    echo 'echo "🔍 Verificando contenido del JAR:"' >> /app/start.sh && \
    echo 'jar tf app.jar | grep -E "Application|Main|Controller" | head -10 || echo "No se encontraron clases principales"' >> /app/start.sh && \
    echo 'echo ""' >> /app/start.sh && \
    echo 'echo "🚀 EJECUTANDO APLICACIÓN"' >> /app/start.sh && \
    echo 'echo "========================================="' >> /app/start.sh && \
    echo 'exec java -jar app.jar --server.port=${PORT:-8080}' >> /app/start.sh && \
    chmod +x /app/start.sh

# Exponer puerto
EXPOSE 8080

# Ejecutar script de inicio
CMD ["/app/start.sh"]