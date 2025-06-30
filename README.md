# HealthTrack Platform

## Descripción
Plataforma web para el monitoreo del peso de usuarios. Permite registro de usuarios y actualización de peso cada 48 horas.

## Problema Identificado
El sistema tenía un bug crítico donde cada actualización de peso restaba automáticamente 1 kg en lugar de registrar el valor ingresado.

## Solución Implementada
- Corrección del método `actualizarPeso()` en la clase `Usuario`
- Implementación de suite completa de pruebas automatizadas
- Pipeline de CI/CD con GitHub Actions
- Validaciones de peso con la clase `PesoValidator`

## Estructura de Pruebas

### Pruebas Unitarias (JUnit 5)
- `UsuarioTest.java` - Pruebas de la lógica de usuario
- `PesoValidatorTest.java` - Validaciones de peso
- Cobertura a alcanzar: >80%

### Pruebas de Integración
- `UsuarioServiceIntegrationTest.java` - Flujos completos de servicio
- Pruebas con base de datos en memoria

### Pruebas Funcionales (Selenium)
- `ActualizacionPesoTest.java` - Flujos E2E de actualización
- `UsuarioRegistroTest.java` - Registro de usuarios
- Ejecución en modo headless para CI/CD

### Pruebas de Rendimiento (JMeter)
- Script: `HealthTrack-Performance-Test.jmx`
- Criterios: <2s response time, <5% error rate
- Carga: 50 usuarios concurrentes

## Pipeline CI/CD

### Stages
1. **Unit Tests** - Pruebas unitarias y análisis SonarQube
2. **Integration Tests** - Pruebas de integración con BD
3. **Functional Tests** - Pruebas E2E con Selenium
4. **Performance Tests** - Validación de rendimiento con JMeter
5. **Build & Package** - Construcción de artefactos y Docker
6. **Security Scan** - Análisis de vulnerabilidades con Trivy
7. **Deploy Staging** - Despliegue automático a staging
8. **Deploy Production** - Despliegue manual a producción

### Herramientas
- **CI/CD**: GitHub Actions
- **Quality Gate**: SonarQube
- **Testing**: JUnit 5, Selenium, JMeter
- **Security**: Trivy, SARIF
- **Notifications**: Slack integration

## Cómo Ejecutar

### Prerrequisitos
- Java 21
- Maven 3.8+
- Docker (opcional)

### Comandos
```bash
# Pruebas unitarias
mvn test

# Pruebas de integración
mvn verify -P integration-tests

# Pruebas funcionales
mvn test -P functional-tests

# Ejecutar aplicación
mvn spring-boot:run

# Build completo
mvn clean package
```

### Docker
```bash
# Build imagen
docker build -t healthtrack .

# Ejecutar contenedor
docker run -p 8080:8080 healthtrack
```

## Métricas de Calidad
- Cobertura de código: >80%
- Complejidad ciclomática: <10
- Duplicación: <3%
- Bugs críticos: 0
- Vulnerabilidades: 0

## Contribuir
1. Fork del repositorio
2. Crear branch feature
3. Implementar cambios con tests
4. Crear Pull Request
5. Pipeline debe pasar completamente