#!/bin/bash

echo "🚀 Script de despliegue HealthTrack"

ENVIRONMENT=${1:-staging}
VERSION=${2:-latest}

log() {
    echo -e "\033[0;32m[$(date +'%Y-%m-%d %H:%M:%S')] $1\033[0m"
}

error() {
    echo -e "\033[0;31m[ERROR] $1\033[0m"
}

if [ "$ENVIRONMENT" = "production" ]; then
    read -r -p "⚠️ ¿Confirmas el despliegue a PRODUCCIÓN? (yes/no): " CONFIRM
    if [ "$CONFIRM" != "yes" ]; then
        error "Despliegue cancelado"
        exit 1
    fi
fi

log "Desplegando a $ENVIRONMENT con versión $VERSION"

# Build de la aplicación
log "Construyendo aplicación..."
mvn clean package -DskipTests

# Build de imagen Docker
log "Construyendo imagen Docker..."
docker build -t healthtrack:"$VERSION" .

# Deploy según ambiente
if [ "$ENVIRONMENT" = "staging" ]; then
    log "Desplegando a staging..."
    docker-compose -f docker-compose.staging.yml up -d
elif [ "$ENVIRONMENT" = "production" ]; then
    log "Desplegando a producción..."
    # Aquí irían comandos específicos de producción
    # kubectl apply -f k8s/
    docker-compose -f docker-compose.prod.yml up -d
fi

# Verificar salud del despliegue
log "Verificando salud del despliegue..."
sleep 30

if curl -f http://localhost:8080/health; then
    log "✅ Despliegue exitoso!"
else
    error "❌ Despliegue falló - health check failed"
    exit 1
fi

log "🎉 Despliegue completado exitosamente!"