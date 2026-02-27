#!/bin/bash
set -e  # Detener en caso de error

echo "========================================="
echo "🚀 INICIANDO DIAGNÓSTICO EN RAILWAY"
echo "========================================="
echo "📅 Fecha: $(date)"
echo "📂 Directorio actual: $(pwd)"
echo "👤 Usuario: $(whoami)"
echo ""

echo "📋 Permisos de archivos:"
ls -la
echo ""

echo "📋 Contenido de target/:"
if [ -d "target" ]; then
    ls -la target/
else
    echo "❌ ERROR: El directorio target/ NO EXISTE"
    echo "Creando directorio target/..."
    mkdir -p target
fi
echo ""

echo "🔍 Buscando archivo JAR..."
JAR_COUNT=$(ls target/*.jar 2>/dev/null | wc -l)
if [ "$JAR_COUNT" -eq 0 ]; then
    echo "❌ ERROR: No se encontraron archivos .jar en target/"
    echo "Contenido completo del proyecto:"
    find . -name "*.jar" -type f 2>/dev/null || echo "No se encontraron JARs"
    exit 1
fi

JAR_FILE=$(ls target/*.jar 2>/dev/null | head -1)
echo "✅ JAR encontrado: $JAR_FILE"
echo "📏 Tamaño: $(ls -lh $JAR_FILE | awk '{print $5}')"
echo ""

echo "🔍 Verificando el JAR (clases principales):"
jar tf $JAR_FILE | grep -E "Application|Main|Controller" | head -10 || echo "No se encontraron clases Application/Main/Controller"
echo ""

echo "☕ Versión de Java:"
java -version 2>&1
echo ""

echo "🌍 Variables de entorno:"
echo "PORT: $PORT"
echo "JAVA_HOME: $JAVA_HOME"
echo "PATH: $PATH"
echo ""

echo "🔧 Verificando conectividad de red:"
echo "Hostname: $(hostname)"
echo "IP: $(hostname -I 2>/dev/null || echo 'No disponible')"
echo ""

echo "========================================="
echo "🚀 EJECUTANDO APLICACIÓN"
echo "========================================="
echo "Comando: java -jar $JAR_FILE --server.port=$PORT"
echo ""

# Ejecutar la aplicación con salida completa
exec java -jar $JAR_FILE --server.port=$PORT 2>&1