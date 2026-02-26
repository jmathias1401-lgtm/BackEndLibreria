#!/bin/bash
set -e  # Detener en caso de error

echo "========================================="
echo "INICIANDO DIAGNÓSTICO - $(date)"
echo "========================================="

# Mostrar información del sistema
echo "1. Directorio actual: $(pwd)"
echo "2. Contenido del directorio:"
ls -la

echo "3. Contenido de target/:"
ls -la target/

echo "4. Variables de entorno importantes:"
echo "PORT: $PORT"
echo "JAVA_HOME: $JAVA_HOME"

echo "5. Versión de Java:"
java -version

echo "6. Buscando JAR en target/:"
JAR_FILE=$(ls target/*.jar 2>/dev/null | head -1)
if [ -z "$JAR_FILE" ]; then
    echo "❌ ERROR: No se encontró ningún archivo .jar en target/"
    exit 1
fi
echo "✅ JAR encontrado: $JAR_FILE"
echo "Tamaño: $(ls -lh $JAR_FILE | awk '{print $5}')"

echo "7. Intentando ejecutar JAR con diagnóstico:"
echo "Comando: java -jar $JAR_FILE --debug"

echo "========================================="
echo "INICIANDO APLICACIÓN"
echo "========================================="

# Ejecutar la aplicación
exec java -jar $JAR_FILE --debug