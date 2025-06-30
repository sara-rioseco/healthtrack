#!/bin/bash

echo "🚀 Ejecutando suite completa de pruebas HealthTrack"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para logging
log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

error() {
    echo -e "${RED}[ERROR] $1${NC}"
}

warn() {
    echo -e "${YELLOW}[WARN] $1${NC}"
}

# Verificar prerrequisitos
log "Verificando prerrequisitos..."
if ! command -v java &> /dev/null; then
    error "Java no está instalado"
    exit 1
fi

if ! command -v mvn &> /dev/null; then
    error "Maven no está instalado"
    exit 1
fi

# Limpiar artefactos previos
log "Limpiando artefactos previos..."
mvn clean

# Ejecutar pruebas unitarias
log "Ejecutando pruebas unitarias..."
if mvn test -B; then
    log "✅ Pruebas unitarias exitosas"
else
    error "❌ Pruebas unitarias fallaron"
    exit 1
fi

# Ejecutar pruebas de integración
log "Ejecutando pruebas de integración..."
if mvn verify -P integration-tests -B; then
    log "✅ Pruebas de integración exitosas"
else
    error "❌ Pruebas de integración fallaron"
    exit 1
fi

# Verificar si la aplicación está corriendo para pruebas funcionales
log "Verificando si la aplicación está disponible..."
if curl -f http://localhost:8080/health &> /dev/null; then
    log "Aplicación disponible, ejecutando pruebas funcionales..."
    if mvn test -P functional-tests -B; then
        log "✅ Pruebas funcionales exitosas"
    else
        warn "⚠️ Pruebas funcionales fallaron (posiblemente aplicación no disponible)"
    fi
else
    warn "⚠️ Aplicación no disponible en localhost:8080, saltando pruebas funcionales"
fi

# Generar reportes
log "Generando reportes de cobertura..."
mvn jacoco:report

# Verificar cobertura
COVERAGE=$(grep -o '<counter type="LINE".*covered="[0-9]*".*missed="[0-9]*"' target/site/jacoco/jacoco.xml | head -1 | grep -o 'covered="[0-9]*"' | grep -o '[0-9]*')
MISSED=$(grep -o '<counter type="LINE".*covered="[0-9]*".*missed="[0-9]*"' target/site/jacoco/jacoco.xml | head -1 | grep -o 'missed="[0-9]*"' | grep -o '[0-9]*')

if [ -n "$COVERAGE" ] && [ -n "$MISSED" ]; then
    TOTAL=$((COVERAGE + MISSED))
    PERCENTAGE=$((COVERAGE * 100 / TOTAL))
    log "Cobertura de código: ${PERCENTAGE}%"

    if [ $PERCENTAGE -ge 80 ]; then
        log "✅ Cobertura cumple el objetivo (≥80%)"
    else
        warn "⚠️ Cobertura por debajo del objetivo (${PERCENTAGE}% < 80%)"
    fi
fi

log "🎉 Suite de pruebas completada!"
log "📊 Reportes disponibles en:"
log "   - target/surefire-reports/ (Pruebas unitarias)"
log "   - target/failsafe-reports/ (Pruebas integración)"
log "   - target/site/jacoco/ (Cobertura de código)"