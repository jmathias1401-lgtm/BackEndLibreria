#!/bin/bash
set -e

echo "========================================="
echo "🚀 INICIANDO DIAGNÓSTICO EN RAILWAY"
echo "========================================="
echo "📅 Fecha: $(date)"
echo "📂 Directorio actual: $(pwd)"
echo ""

echo "📋 Contenido del directorio raíz:"
ls -la
echo ""

echo "📋 Contenido de target/:"
if [ -d "target" ]; then
    ls -la target/
else
    echo "❌ El directorio target/ no existe"
    exit 1
fi
echo ""

echo "☕ Versión de Java:"
java -version 2>&1
echo ""

echo "🔍 Buscando archivo JAR..."
JAR_FILE=$(ls target/*.jar 2>/dev/null | head -1)

if [ -z "$JAR_FILE" ]; then
    echo "❌ ERROR: No se encontró ningún archivo .jar en target/"
    exit 1
fi

echo "✅ JAR encontrado: $JAR_FILE"
echo "📏 Tamaño: $(ls -lh $JAR_FILE | awk '{print $5}')"
echo ""

echo "🔍 Verificando el JAR (primeras 20 líneas):"
jar tf $JAR_FILE | head -20
echo ""

echo "🔍 Buscando clase principal en el JAR:"
MAIN_CLASS=$(jar tf $JAR_FILE | grep -E "Application|Main" | head -5)
if [ -n "$MAIN_CLASS" ]; then
    echo "✅ Clases encontradas:"
    echo "$MAIN_CLASS"
else
    echo "⚠️ No se encontraron clases con 'Application' o 'Main'"
fi
echo ""

echo "🌍 Variables de entorno:"
echo "PORT: $PORT"
echo "SECRET_KEY: ${SECRET_KEY:0:10}... (truncado)"
echo ""

echo "========================================="
echo "🚀 EJECUTANDO APLICACIÓN"
echo "========================================="

# Ejecutar con redirección de salida
exec java -jar $JAR_FILE --debug 2>&1