package com.healthtrack.unit;

import com.healthtrack.model.PesoValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PesoValidator - Unit Tests")
class PesoValidatorTest {

    @Test
    @DisplayName("Validar peso negativo debería lanzar excepción")
    void testValidarPeso_ConPesoNegativo_DeberiaLanzarExcepcion() {
        // Given
        double pesoNegativo = -5.0;

        // When & Then
        assertThatThrownBy(() -> PesoValidator.validarPeso(pesoNegativo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El peso no puede ser menor a");
    }

    @Test
    @DisplayName("Validar peso excesivo debería lanzar excepción")
    void testValidarPeso_ConPesoExcesivo_DeberiaLanzarExcepcion() {
        // Given
        double pesoExcesivo = 600.0;

        // When & Then
        assertThatThrownBy(() -> PesoValidator.validarPeso(pesoExcesivo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El peso no puede ser mayor a");
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0, 50.5, 75.0, 100.3, 500.0})
    @DisplayName("Validar peso válido debería pasar sin excepción")
    void testValidarPeso_ConPesoValido_DeberiaPassar(double pesoValido) {
        // When & Then
        assertThatCode(() -> PesoValidator.validarPeso(pesoValido))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("esPesoValido debería retornar true para peso válido")
    void testEsPesoValido_ConPesoValido_DeberiaRetornarTrue() {
        // Given
        double pesoValido = 75.0;

        // When
        boolean resultado = PesoValidator.esPesoValido(pesoValido);

        // Then
        assertThat(resultado).isTrue();
    }

    @Test
    @DisplayName("esPesoValido debería retornar false para peso inválido")
    void testEsPesoValido_ConPesoInvalido_DeberiaRetornarFalse() {
        // Given
        double pesoInvalido = -10.0;

        // When
        boolean resultado = PesoValidator.esPesoValido(pesoInvalido);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Validar peso límite mínimo debería ser válido")
    void testValidarPeso_LimiteMinimo() {
        // Given
        double pesoMinimo = 1.0;

        // When & Then
        assertThatCode(() -> PesoValidator.validarPeso(pesoMinimo))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Validar peso límite máximo debería ser válido")
    void testValidarPeso_LimiteMaximo() {
        // Given
        double pesoMaximo = 500.0;

        // When & Then
        assertThatCode(() -> PesoValidator.validarPeso(pesoMaximo))
                .doesNotThrowAnyException();
    }
}